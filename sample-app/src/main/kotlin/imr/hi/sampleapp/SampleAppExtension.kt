package imr.hi.sampleapp

import com.fasterxml.jackson.databind.ObjectMapper
import imr.hi.kartverket.AdresserLocationServiceProvider
import imr.hi.kartverket.StedsnavnLocationServiceProvider
import imr.hi.mareano.database.Database
import imr.hi.mareano.database.DatabaseConfig
import imr.hi.mareano.extensions.AppContext
import imr.hi.mareano.extensions.AppExtension
import imr.hi.mareano.extensions.Contribution
import imr.hi.mareano.extensions.LifecycleContribution
import imr.hi.mareano.extensions.RouteContribution
import imr.hi.mareano.frontend.FrontendContribution
import imr.hi.mareano.frontend.FrontendModuleScript
import imr.hi.search.LocationSearchService
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.ngu.kartverket.api.stedsnavn.StedsnavnHttpService
import java.net.URI
import java.net.http.HttpClient

private val jsonMapper = ObjectMapper().findAndRegisterModules()

class SampleAppExtension : AppExtension {

    override val id: String = "sample-extension"

    override fun contributions(context: AppContext): List<Contribution<*>> {
        val locationSearchService = LocationSearchService(
            StedsnavnLocationServiceProvider(),
            AdresserLocationServiceProvider()
        )

        return listOf(
            Contribution(RouteContribution::class, ApiRoutes(locationSearchService)),
            Contribution(FrontendContribution::class, FrontendScripts())
        )
    }
}

private class FrontendScripts : FrontendContribution {
    override fun scripts(context: AppContext): List<FrontendModuleScript> = listOf(
        FrontendModuleScript(
            scriptPath = "/extensions/sample/sample-extension.js",
            resourcePath = "frontend-extensions/sample/sample-extension.js",
        ),
    )
}

private class ApiRoutes(
    private val locationSearchService: LocationSearchService,
) : RouteContribution {

    override fun pathPrefixes(): List<String> = listOf("api/location-search")

    override fun install(
        route: Route,
        context: AppContext,
    ) {
        route.route("api") {
            get("location-search") {
                val q = call.request.queryParameters["q"]
                val query = q ?: call.request.queryParameters["text"]
                if (query.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "A query parameter is required: 'q' or 'text' (search term)",
                    )
                    return@get
                }
                val result = locationSearchService.search(query.trim())
                call.respondText(
                    jsonMapper.writeValueAsString(result),
                    ContentType.Application.Json,
                )
            }
        }
    }
}
