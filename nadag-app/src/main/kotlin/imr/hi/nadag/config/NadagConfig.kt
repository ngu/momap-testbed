package imr.hi.nadag.config

data class NadagConfig(
    val db: NadagDbConfig,
)

data class NadagDbConfig(
    val url: String,
)
