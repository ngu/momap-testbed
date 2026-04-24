package imr.hi.nadag

import com.fasterxml.jackson.databind.ObjectMapper
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
import imr.hi.nadag.search.NadagRepository
import imr.hi.nadag.search.NadagSearchService
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

private val jsonMapper = ObjectMapper().findAndRegisterModules()

class NadagAppExtension : AppExtension {

    override val id: String = "nadag"

    override fun contributions(context: AppContext): List<Contribution<*>> {
        val nadagConfig = NadagConfigFactory.create(context.appConfig.environment)
        val nadagDatabase = Database(
            DatabaseConfig(
                jdbcUrl = nadagConfig.db.url,
                maximumPoolSize = 5,
            ),
        )
        val nadagSearchService = NadagSearchService(NadagRepository(nadagDatabase))

        return listOf(
            Contribution(RouteContribution::class, NadagApiRoutes(nadagSearchService)),
            Contribution(FrontendContribution::class, NadagFrontendScripts()),
            Contribution(LifecycleContribution::class, NadagLifecycle(nadagDatabase)),
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

private class NadagApiRoutes(
    private val nadagService: NadagSearchService,
) : RouteContribution {

    override fun routeContextPath(): String = "api"

    override fun pathPrefixes(): List<String> = listOf("api/nadag")

    override fun install(
        route: Route,
        context: AppContext,
    ) {
        route.route("api/nadag") {
            get("search") {
                val projectSearch = call.request.queryParameters["prosjekt"]
                if (projectSearch.isNullOrBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "At least one query parameter is required: 'prosjekt' (project name or number)",
                    )
                    return@get
                }
                val result = nadagService.byProsjektnavnOrProsjektnr(projectSearch.trim(), projectSearch.trim())
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
