package com.gymfit.backend

import io.ktor.application.Application
import io.ktor.server.engine.EngineAPI
import io.ktor.server.engine.embeddedServer

import io.grpc.stub.StreamObserver


@EngineAPI
fun main(args: Array<String>): Unit {
    embeddedServer(GrpcFactory, configure = {
        port = 4000
        configure = {
            addService(AuthenticationServiceImpl())
        }
    }){}.start(true)
}

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
}

class AuthenticationServiceImpl: AuthenticationServiceGrpc.AuthenticationServiceImplBase() {
    override fun signInAnonymous(request: SignInAnonymousRequest?, responseObserver: StreamObserver<SignInAnonymousResponse>?) {
        super.signInAnonymous(request, responseObserver)
    }
}
