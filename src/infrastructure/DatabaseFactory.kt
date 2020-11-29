package com.gymfit.backend.infrastructure

import com.typesafe.config.ConfigFactory

import io.ktor.util.KtorExperimentalAPI
import io.ktor.config.HoconApplicationConfig

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

import org.jetbrains.exposed.sql.Table

@KtorExperimentalAPI
object DatabaseFactory {

    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val url = appConfig.property("database.url").getString()
    private val driver = appConfig.property("database.driver").getString()
    private val username = appConfig.property("database.username").getString()
    private val password = appConfig.property("database.password").getString()

    fun init(tables: Array<Table>): DatabaseFactory {
        try {
            Database.connect(url, driver = driver, user = username, password = password)

            transaction {
                SchemaUtils.createMissingTablesAndColumns(*tables)
            }

            return this
        } catch (error: Exception) {
            print("Database connection error ${error.message}")
            throw error
        }
    }

    suspend fun <T> query(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}