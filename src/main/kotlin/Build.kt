import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.run
import model.builder.BuilderResponse
import model.config.Configuration
import model.config.ConfigurationResponse
import okhttp3.*
import org.jetbrains.ktor.features.logInfo
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.locations.post
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.Route
import java.io.IOException


private val BUILDER_CRUMB = System.getenv("BUILDER_CRUMB")
private val BUILDER_URL = System.getenv("BUILDER_URL")
private val BUILDER_USERNAME = System.getenv("BUILDER_USERNAME")
private val BUILDER_PASSWORD = System.getenv("BUILDER_PASSWORD")

fun Route.build(client: OkHttpClient) {
    post<Build> {
        try {
            run(CommonPool) {
                val config = call.receive<Configuration>()
                config.setConfigs()
                val builderResponse = startBuilder(client, config.configs())
                call.respond(ConfigurationResponse(builderResponse.success, builderResponse.response))
            }
        } catch (e: Exception) {
            App.logger.error(call.request.logInfo(), e)
            call.respondText(
                    Gson().toJson(ConfigurationResponse(false, e.localizedMessage)),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
            )
        }
    }
}

private suspend fun startBuilder(client: OkHttpClient, configs: MutableMap<String, String>): BuilderResponse {
    val builder = MultipartBody.Builder()
    builder.setType(MultipartBody.FORM)

    for (config in configs) {
        builder.addFormDataPart(config.key, config.value)
    }

    val buildRequest = Request.Builder()
            .url(BUILDER_URL)
            .addHeader("Authorization", Credentials.basic(BUILDER_USERNAME, BUILDER_PASSWORD))
            .addHeader("Jenkins-Crumb", BUILDER_CRUMB)
            .post(builder.build())
            .build()

    client.newCall(buildRequest).execute().use { buildResponse ->
        return try {
            if (!buildResponse.isSuccessful || buildResponse.body() == null) {
                App.logger.error(buildResponse.message())
                throw IOException("Unexpected code " + buildResponse)
            }
            BuilderResponse(true, buildResponse.message())
        } catch (e: Exception) {
            App.logger.error(e.localizedMessage, buildRequest.body())
            BuilderResponse(false, e.localizedMessage)
        }
    }
}