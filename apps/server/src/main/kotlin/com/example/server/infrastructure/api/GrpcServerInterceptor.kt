package com.example.server.infrastructure.api

import io.grpc.*
import io.grpc.kotlin.CoroutineContextServerInterceptor
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
//import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.cloud.sleuth.Tracer
import org.springframework.cloud.sleuth.instrument.kotlin.asContextElement
import kotlin.coroutines.CoroutineContext

@GrpcGlobalServerInterceptor
class GrpcServerInterceptor : ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val logger = LoggerFactory.getLogger(this::class.java)
        logger.info(headers.toString())
        val ctx = Context.current()
        return Contexts.interceptCall(ctx, call, headers, next)
    }
}

@GrpcGlobalServerInterceptor
class TracerCoroutineContextServerInterceptor(
    private val tracer: Tracer
): CoroutineContextServerInterceptor() {
    override fun coroutineContext(call: ServerCall<*, *>, headers: Metadata): CoroutineContext {
        return tracer.asContextElement()
    }
}