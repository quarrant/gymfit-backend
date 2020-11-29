package com.gymfit.backend

import io.ktor.server.engine.EngineAPI
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI

import com.gymfit.backend.domain.entities.*
import com.gymfit.backend.domain.repositories.*
import com.gymfit.backend.infrastructure.grpc.GrpcFactory
import com.gymfit.backend.infrastructure.database.DatabaseFactory
import com.gymfit.backend.presenters.*


@EngineAPI
@KtorExperimentalAPI
fun main(args: Array<String>) {
    val database = DatabaseFactory.init(arrayOf(AccountsTable))

    val accountsRepository = AccountsRepository(database, AccountsTable)

    embeddedServer(GrpcFactory, configure = {
        port = 4000
        configure = {
            addService(AuthenticationGrpcService())
            addService(AccountGrpcService(accountsRepository))
        }
    }) {}.start(true)
}