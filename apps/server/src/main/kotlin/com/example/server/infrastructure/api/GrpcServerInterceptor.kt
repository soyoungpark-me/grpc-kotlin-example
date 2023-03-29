package com.example.server.infrastructure.api

import brave.Tracer
import brave.Tracing
import brave.grpc.GrpcTracing
import io.grpc.*
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.sleuth.Span
//import org.springframework.cloud.sleuth.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//@Configuration
//class ServiceBReactorStubConfiguration {
//
//    @Bean
//    fun grpcTracing(tracing: Tracing): GrpcTracing {
//        return GrpcTracing.create(tracing)
//    }
//
//}

//class GrpcServerInterceptor(private val tracing: GrpcTracing) : ServerInterceptor {
//    override fun <ReqT : Any, RespT : Any> interceptCall(
//        call: ServerCall<ReqT, RespT>,
//        headers: Metadata,
//        next: ServerCallHandler<ReqT, RespT>
//    ): ServerCall.Listener<ReqT> {
//        val logger = LoggerFactory.getLogger(this::class.java)
//        val span = tracing.
//        val span2 = tracer.nextSpan()
//
//        val original = next.startCall(call, headers)
//        return object : SimpleForwardingServerCallListener<ReqT>(original) {
//            override fun onComplete() {
////                tracer.continueSpan(span)
////                span.logEvent(Span.SERVER_SEND)
////                tracer.close(span)
//                super.onComplete()
//
//            }
//        }
//    }
//}