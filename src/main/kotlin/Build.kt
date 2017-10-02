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
import org.jetbrains.ktor.request.receiveText
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
                val config = Gson().fromJson(call.receiveText(), Configuration::class.java)
                val builderResponse = startBuilder(client, ConfigMap(config).configsMap)
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

class ConfigMap(configuration: Configuration) {
    var configsMap: MutableMap<String, String> = HashMap()

    init {
        configsMap.put("titleText", configuration.titleText)
        configsMap.put("titleTextSize", configuration.titleTextSize)
        configsMap.put("titleTextColor", configuration.titleTextColor)
        configsMap.put("imageUrl", configuration.imageUrl)
        configsMap.put("imageWidth", configuration.imageWidth)
        configsMap.put("imageHeight", configuration.imageHeight)
        configsMap.put("imageScaleType", configuration.imageScaleType)
        configsMap.put("copyText", configuration.copyText)
        configsMap.put("copyTextSize", configuration.copyTextSize)
        configsMap.put("copyTextColor", configuration.copyTextColor)
        configsMap.put("sendButtonText", configuration.sendButtonText)
        configsMap.put("sendButtonTextColor", configuration.sendButtonTextColor)
        configsMap.put("sendButtonBackgroundColor", configuration.sendButtonBackgroundColor)
        configsMap.put("phoneInputTextColor", configuration.phoneInputTextColor)
        configsMap.put("phoneInputBackgroundColor", configuration.phoneInputBackgroundColor)
        configsMap.put("numPadTextColor", configuration.numPadTextColor)
        configsMap.put("numPadBackgroundColor", configuration.numPadBackgroundColor)
        configsMap.put("backgroundColor", configuration.backgroundColor)
        configsMap.put("pin", configuration.pin)
        configsMap.put("message", configuration.message)
        configsMap.put("messagingUrlBase", configuration.messagingUrlBase)
        configsMap.put("messagingUrlPath", configuration.messagingUrlPath)
        configsMap.put("builderUrlBase", configuration.builderUrlBase)
        configsMap.put("builderUrlPath", configuration.builderUrlPath)
        configsMap.put("email", configuration.email)
    }
}
