package imr.hi.nadag.config

object NadagConfigFactory {
    fun create(environment: String): NadagConfig {
        return if (environment == "local") {
            NadagConfig(
                db = NadagDbConfig(
                    url = envOrDefault("NADAG_DB_URL", "jdbc:postgresql://localhost:5442/nadag?user=nadag&password=nadag"),
                ),
            )
        } else {
            NadagConfig(
                db = NadagDbConfig(
                    url = requireEnv("NADAG_DB_URL"),
                ),
            )
        }
    }

    private fun envOrDefault(key: String, default: String): String = System.getenv(key)
        ?.takeIf { it.isNotBlank() }
        ?: default

    private fun requireEnv(key: String): String = requireNotNull(System.getenv(key)?.takeIf { it.isNotBlank() }) {
        "$key must be set"
    }
}
