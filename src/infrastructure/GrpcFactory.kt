package com.gymfit.backend.infrastructure

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

class ExceptionInterceptor : ServerInterceptor {

    /**
     * When closing a gRPC call, extract any error status information to top-level fields. Also
     * log the cause of errors.
     */
    private class ExceptionTranslatingServerCall<ReqT, RespT>(
        delegate: ServerCall<ReqT, RespT>
    ) : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(delegate) {

        override fun close(status: Status, trailers: Metadata) {
            if (status.isOk) {
                return super.close(status, trailers)
            }
            val cause = status.cause
            var newStatus = status

//            logger.error(cause) { "Error handling gRPC endpoint." }

            if (status.code == Status.Code.UNKNOWN) {
                val translatedStatus = when (cause) {
                    is IllegalArgumentException -> Status.INVALID_ARGUMENT
                    is IllegalStateException -> Status.FAILED_PRECONDITION
                    else -> Status.UNKNOWN
                }
                newStatus = translatedStatus.withDescription(cause?.message).withCause(cause)
            }

            super.close(newStatus, trailers)
        }
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        return next.startCall(ExceptionTranslatingServerCall(call), headers)
    }

}