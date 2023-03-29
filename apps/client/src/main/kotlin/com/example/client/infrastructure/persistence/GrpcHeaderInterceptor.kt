package com.example.client.infrastructure.persistence

import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import org.slf4j.LoggerFactory

class GrpcHeaderInterceptor() : ClientInterceptor {

    override fun <ReqT : Any, RespT : Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        val logger = LoggerFactory.getLogger(this::class.java)

        return object : SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>?, headers: Metadata) {
                /* put custom header */
                headers.put(Metadata.Key.of("X-USER-ID", Metadata.ASCII_STRING_MARSHALLER), "test")

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