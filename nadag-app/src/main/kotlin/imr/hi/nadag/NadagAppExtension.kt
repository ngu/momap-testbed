package imr.hi.nadag

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
import imr.hi.nadag.config.NadagConfigFactory
// import no.ngu.nadag.NadagRepository
// import no.ngu.nadag.NadagService
import imr.hi.search.LocationSearchService
import imr.hi.nadag.NadagLocationSearchProvider
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

class NadagAppExtension : AppExtension {

    override val id: String = "nadag"

    override fun contributions(context: AppContext): List<Contribution<*>> {
        // val nadagConfig = NadagConfigFactory.create(context.appConfig.environment)
        // val nadagDatabase = Database(
        //     DatabaseConfig(
        //         jdbcUrl = nadagConfig.db.url,
        //         maximumPoolSize = 5,
        //     ),
        // )
        // val nadagService = NadagService(NadagRepository(nadagDatabase))
        val locationSearchService = LocationSearchService(
            StedsnavnLocationServiceProvider(),
            AdresserLocationServiceProvider(),
            // NadagLocationSearchProvider(nadagService),
        )

        return listOf(
            Contribution(RouteContribution::class, ApiRoutes(locationSearchService)),
            Contribution(FrontendContribution::class, NadagFrontendScripts()),
            // Contribution(LifecycleContribution::class, NadagLifecycle(nadagDatabase)),
        )
    }
}

private class NadagFrontendScripts : FrontendContribution {
    override fun scripts(context: AppContext): List<FrontendModuleScript> = listOf(
        FrontendModuleScript(
            scriptPath = "/extensions/nadag/nadag-extension.js",
            resourcePath = "frontend-extensions/nadag/nadag-extension.js",
        ),
    )
}

private class ApiRoutes(
    // private val nadagService: NadagService,
    private val locationSearchService: LocationSearchService,
) : RouteContribution {

    override fun routeContextPath(): String = "api"

    override fun pathPrefixes(): List<String> = listOf("api/nadag")

    override fun install(
        route: Route,
        context: AppContext,
    ) {
        // route.route("api/nadag") {
        //     get("search") {
        //         val projectSearch = call.request.queryParameters["prosjekt"]
        //         if (projectSearch.isNullOrBlank()) {
        //             call.respond(
        //                 HttpStatusCode.BadRequest,
        //                 "At least one query parameter is required: 'prosjekt' (project name or number)",
        //             )
        //             return@get
        //         }
        //         val result = nadagService.byProsjektnavnOrProsjektnr(projectSearch.trim(), projectSearch.trim())
        //         call.respondText(
        //             jsonMapper.writeValueAsString(result),
        //             ContentType.Application.Json,
        //         )
        //     }
        // }

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

private class NadagLifecycle(
    private val nadagDatabase: Database,
) : LifecycleContribution {
    override fun onApplicationStopPreparing(
        application: Application,
        context: AppContext,
    ) {
        nadagDatabase.close()
    }
}
