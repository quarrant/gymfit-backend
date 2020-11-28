package com.gymfit.backend

import io.ktor.server.engine.EngineAPI
import io.ktor.server.engine.embeddedServer

import io.grpc.stub.StreamObserver
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import org.slf4j.event.Level


@EngineAPI
fun main(args: Array<String>): Unit {
    embeddedServer(GrpcFactory, configure = {
        port = 4000
        module = Application::module
        configure = {
            addService(AuthenticationServiceImpl())
        }
    }){}.start(true)
}

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
    }
}

class AuthenticationServiceImpl: AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineImplBase() {
    override suspend fun signInAnonymous(request: SignInAnonymousRequest): SignInAnonymousResponse {
        val response = SignInAnonymousResponse.newBuilder()
        return response.build()
    }
}
