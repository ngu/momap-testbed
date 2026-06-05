package imr.hi.sampleapp

import imr.hi.mareano.database.Database
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import kotliquery.queryOf
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory

object KotestProjectConfig :
    AbstractProjectConfig(),
    BeforeProjectListener,
    AfterProjectListener {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun beforeProject() {
        val dbName = databaseConfig.getDatabaseName()
        log.info("Creating test database '$dbName'...")
        try {
            Database(createAdminDatabaseConfig()).use {
                it.run(queryOf("create database \"$dbName\"").asExecute)
            }
        } catch (e: PSQLException) {
            val duplicateDatabase = "42P04"
            if (e.sqlState != duplicateDatabase) throw e
        }
    }

    override suspend fun afterProject() {
        val dbName = databaseConfig.getDatabaseName()
        log.info("Dropping test database '$dbName'...")
        Database(createAdminDatabaseConfig()).use {
            it.run(queryOf("drop database if exists \"$dbName\"").asExecute)
        }
    }
}
