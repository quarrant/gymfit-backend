package com.gymfit.backend

import io.ktor.server.engine.EngineAPI
import io.ktor.server.engine.embeddedServer

import com.gymfit.backend.infrastructure.*
import com.gymfit.backend.presenters.*

@EngineAPI
fun main(args: Array<String>) {
//    var db = DatabaseFactory.init()
    embeddedServer(GrpcFactory, configure = {
        port = 4000
        configure = {
            addService(AuthenticationGrpcService())
            addService(AccountGrpcService())
        }
    }) {}.start(true)
}