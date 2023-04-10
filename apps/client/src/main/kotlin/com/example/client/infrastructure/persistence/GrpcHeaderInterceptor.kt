package com.example.client.infrastructure.persistence

import com.example.client.application.AuditService
import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor
import org.slf4j.LoggerFactory

@GrpcGlobalClientInterceptor
class GrpcHeaderInterceptor(
    private val auditService: AuditService
) : ClientInterceptor {

    override fun <ReqT : Any, RespT : Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        val logger = LoggerFactory.getLogger(this::class.java)

        return object : SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>?, headers: Metadata) {
                /* put custom header */
                headers.put(Metadata.Key.of("X-USER-ID", Metadata.ASCII_STRING_MARSHALLER), auditService.getAuditId())

                super.start(object : SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onHeaders(headers: Metadata) {
                        super.onHeaders(headers)
                    }
                }, headers)
            }

            override fun halfClose() {
                logger.info("[Client Interceptor] Invoke RPC - ${method?.fullMethodName}")
                super.halfClose()
            }
        }
    }
}