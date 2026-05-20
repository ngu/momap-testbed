package imr.hi.nadag

import com.fasterxml.jackson.databind.ObjectMapper
import imr.hi.nadag.withTestApplication
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

private val jsonMapper = ObjectMapper().findAndRegisterModules()

class LocationSearchRouteTest : FunSpec({
    context("Nadag Search Route") {
        test("search prosjekt=Gk00517 returns expected project row from search route")
            .config(enabled = !System.getenv("NADAG_DB_URL").isNullOrBlank()) {
            withTestApplication {
                val response = client.get("api/location-search?q=Stangvik")
                response.status shouldBe HttpStatusCode.OK
            }
        }
    }
})
