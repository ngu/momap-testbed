package imr.hi.nadag

import imr.hi.mareano.config.AppConfigFactory
import imr.hi.mareano.createServer

fun main() {
    val appConfig = AppConfigFactory.create()
    createServer(appConfig).start(wait = true)
}
