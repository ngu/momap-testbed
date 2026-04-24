package imr.hi.nadag.search

import com.fasterxml.jackson.databind.ObjectMapper
import imr.hi.nadag.withTestApplication
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

private val jsonMapper = ObjectMapper().findAndRegisterModules()

class NadagSearchRouteTest : FunSpec({
    context("Nadag Search Route") {
        test("search prosjekt=Gk00517 returns expected project row from search route")
            .config(enabled = !System.getenv("NADAG_DB_URL").isNullOrBlank()) {
            withTestApplication {
                val response = client.get("api/nadag/search?prosjekt=${NadagSearchTest.sampleNadagProject.prosjektnavn()}")
                
                response.status shouldBe HttpStatusCode.OK
                
                val listType = jsonMapper.typeFactory.constructCollectionType(List::class.java, NadagProject::class.java)
                val projects: List<NadagProject> = jsonMapper.readValue(response.bodyAsText(), listType)
                projects.shouldContain(NadagSearchTest.sampleNadagProject)
            }
        }
    }
})
