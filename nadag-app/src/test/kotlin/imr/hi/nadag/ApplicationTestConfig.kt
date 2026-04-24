package imr.hi.nadag

import imr.hi.mareano.config.AppConfig
import imr.hi.mareano.config.ServerConfig
import imr.hi.mareano.config.TaskConfig
import imr.hi.mareano.configure
import imr.hi.mareano.database.DatabaseConfig
import imr.hi.mareano.flyway.FlywayMigrationManager
import imr.hi.mareano.tasks.SynchronizeMapLayersTask
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.testing.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val nadagDatabaseConfig: DatabaseConfig = createRandomDatabaseConfig("mareano")

fun <R> withTestApplication(
    config: AppConfig = createTestApplicationConfig(),
    additionalConfiguration: (Application.() -> Unit)? = null,
    test: suspend ApplicationTestBuilder.() -> R,
) {
    testApplication {
        application {
            configure(config)
            additionalConfiguration?.invoke(this)
        }

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        test()
    }
}

fun createTestApplicationConfig() = AppConfig(
    environment = "local",
    server = ServerConfig(allowedCorsHosts = emptyList()),
    databaseConfig = nadagDatabaseConfig,
    flyway = FlywayMigrationManager.MigrationConfig(),
    taskConfig = TaskConfig(syncMapLayerTask = SynchronizeMapLayersTask.Config(disabled = true)),
)

fun createRandomDatabaseConfig(prefix: String): DatabaseConfig {
    val port: Int = System.getenv("DATABASE_PORT")?.toIntOrNull() ?: 5442
    val host: String = System.getenv("DATABASE_HOST") ?: "localhost"
    val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    val randomId = (1..8).map { ('a'..'z').random() }.joinToString("")
    val randomDatabaseName = "$prefix-$currentTime-$randomId"
    return DatabaseConfig(
        jdbcUrl = "jdbc:postgresql://$host:$port/$randomDatabaseName?user=mareano&password=mareano",
        schema = null,
        maximumPoolSize = 5,
    )
}

fun createAdminDatabaseConfig(): DatabaseConfig {
    val port: Int = System.getenv("DATABASE_PORT")?.toIntOrNull() ?: 5442
    val host: String = System.getenv("DATABASE_HOST") ?: "localhost"
    return DatabaseConfig(
        jdbcUrl = "jdbc:postgresql://$host:$port/mareano?user=mareano&password=mareano",
        schema = null,
        maximumPoolSize = 2,
    )
}
