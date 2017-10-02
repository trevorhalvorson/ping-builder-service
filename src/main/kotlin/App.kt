import mu.KLogging
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CORS
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.locations.Locations
import org.jetbrains.ktor.locations.location
import org.jetbrains.ktor.routing.Routing

@location("/build")
class Build

class App {

    companion object : KLogging()

    private val DEBUG = false

    fun Application.main() {
        install(DefaultHeaders)
        install(CallLogging)
        install(CORS) {
            anyHost()
        }
        install(Locations)
        install(GsonSupport) {
            setPrettyPrinting()
        }

        val interceptor = HttpLoggingInterceptor()

        if (DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        install(Routing) {
            build(client)
        }
    }

}