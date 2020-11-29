package com.gymfit.backend.infrastructure.grpc

import io.grpc.*
import io.ktor.application.*
import io.ktor.server.engine.*

@EngineAPI
object GrpcFactory: ApplicationEngineFactory<GrpcEngine, GrpcEngine.Configuration> {
    override fun create(environment: ApplicationEngineEnvironment, configure: GrpcEngine.Configuration.() -> Unit): GrpcEngine {
        return GrpcEngine(environment, configure)
    }
}

@EngineAPI
class GrpcEngine(environment: ApplicationEngineEnvironment, configure: Configuration.() -> Unit = {}): BaseApplicationEngine(environment) {

    class Configuration: BaseApplicationEngine.Configuration() {
        var port = 4000
        var configure: ServerBuilder<*>.() -> Unit = {}
    }

    private val configuration = Configuration().apply(configure)
    private var server: Server? = null

    override fun start(wait: Boolean): ApplicationEngine {
        server = ServerBuilder
            .forPort(configuration.port)
            .apply(configuration.configure)
            .intercept(ExceptionInterceptor())
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

