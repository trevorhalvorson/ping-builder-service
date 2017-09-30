import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.run
import model.crumb.Crumb
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


private val BUILDER_CRUMB_URL = System.getenv("BUILDER_CRUMB_URL")
private val BUILDER_URL = System.getenv("BUILDER_URL")
private val BUILDER_USERNAME = System.getenv("BUILDER_USERNAME")
private val BUILDER_PASSWORD = System.getenv("BUILDER_PASSWORD")

fun Route.build(client: OkHttpClient) {
    post<Build> {
        try {
            run(CommonPool) {
                val config = call.receive<Configuration>()
                val builderResponse = startBuilder(client, config.configsMap)
                call.respond(ConfigurationResponse(builderResponse.success, builderResponse.response))
            }
        } catch (e: Exception) {
            App.logger.error(call.request.logInfo(), e)
            call.respondText(
                    Gson().toJson(ConfigurationResponse(false, "Unable to start build")),
                    ContentType.Application.Json,
                    HttpStatusCode.BadRequest
            )
        }
    }
}

private suspend fun startBuilder(client: OkHttpClient, configs: MutableMap<String, String>): BuilderResponse {
    val credentials = Credentials.basic(BUILDER_USERNAME, BUILDER_PASSWORD)

    val crumbRequest = Request.Builder()
            .url(BUILDER_CRUMB_URL)
            .addHeader("Authorization", credentials)
            .get()
            .build()

    var crumb: Crumb? = null
    client.newCall(crumbRequest).execute().use { crumbResponse ->
        if (!crumbResponse.isSuccessful || crumbResponse.body() == null) {
            App.logger.error(crumbResponse.message())
            throw IOException("Unexpected code " + crumbResponse)
        }

        crumb = Gson().fromJson(crumbResponse.body()?.string(), Crumb::class.java)
    }

    val builder = MultipartBody.Builder()
    builder.setType(MultipartBody.FORM)

    for (config in configs) {
        builder.addFormDataPart(config.key, config.value)
    }

    val buildRequest = Request.Builder()
            .url(BUILDER_URL)
            .addHeader("Authorization", credentials)
            .addHeader(crumb!!.crumbRequestField, crumb!!.crumb)
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