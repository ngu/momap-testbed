package imr.hi.sampleapp

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import imr.hi.mareano.config.AzureJwtPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.testing.*
import java.util.*

fun createTestToken(): String {
    return JWT.create()
        .withIssuer("https://localhost:8080")
        .withAudience("mareano-api-local")
        .withSubject("test-user")
        .withClaim("name", "Test User")
        .withClaim("email", "test@example.com")
        .withClaim("preferred_username", "test@example.com")
        .withClaim("scp", "access_as_user")
        .withClaim("roles", listOf("Mareano.Admin"))
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
        .sign(Algorithm.HMAC256("local-dev-secret-placeholder"))
}

fun ApplicationTestBuilder.setupMockAuthentication() {
    application {
        install(Authentication) {
            fun jwtConfig(name: String) {
                jwt(name) {
                    realm = "Test realm"
                    verifier(
                        JWT.require(Algorithm.HMAC256("local-dev-secret-placeholder"))
                            .withIssuer("https://localhost:8080")
                            .withAudience("mareano-api-local")
                            .build(),
                    )
                    validate { credential ->
                        val jwtPrincipal = JWTPrincipal(credential.payload)
                        AzureJwtPrincipal(
                            userId = credential.payload.subject ?: "test-user",
                            jwtPrincipal = jwtPrincipal,
                            name = credential.payload.getClaim("name").asString(),
                            email = credential.payload.getClaim("preferred_username").asString(),
                            scopes = listOf("access_as_user"),
                            roles = credential.payload.getClaim("roles")
                                .asList(String::class.java)
                                ?: emptyList(),
                        )
                    }
                }
            }
            jwtConfig("azure-ad")
            jwtConfig("local-mock")
        }
    }
}
