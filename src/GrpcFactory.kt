package com.gymfit.backend

import io.grpc.Server
import io.grpc.ServerBuilder

import io.ktor.application.*
import io.ktor.server.engine.*

@EngineAPI
object GrpcFactory : ApplicationEngineFactory<GrpcEngine, GrpcConfiguration> {
    override fun create(environment: ApplicationEngineEnvironment, configure: GrpcConfiguration.() -> Unit): GrpcEngine {
        return GrpcEngine(environment, configure)
    }
}

@EngineAPI
class GrpcConfiguration: BaseApplicationEngine.Configuration() {
    var port = 4000
    var configure: ServerBuilder<*>.() -> Unit = {}
    var module: Application.() -> Unit = {}
}

@EngineAPI
class GrpcEngine(environment: ApplicationEngineEnvironment, val configure: GrpcConfiguration.() -> Unit = {}): BaseApplicationEngine(environment) {

    private val configuration = GrpcConfiguration().apply(configure)
    private var server: Server? = null

    override fun start(wait: Boolean): ApplicationEngine {
        server = ServerBuilder
            .forPort(configuration.port)
            .apply(configuration.configure)
            .build().start()

        if (wait) {
            server!!.awaitTermination()
        }

        return this
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
        environment.monitor.raise(ApplicationStopPreparing, environment)
        server!!.awaitTermination()

        if (server != null) {
            server!!.shutdownNow()
        }
    }
}