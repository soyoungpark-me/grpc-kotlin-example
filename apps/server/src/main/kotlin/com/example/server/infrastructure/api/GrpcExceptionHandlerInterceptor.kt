package com.example.server.infrastructure.api

import com.google.rpc.Code
import com.google.rpc.ErrorInfo
import io.grpc.*
import io.grpc.protobuf.StatusProto
import org.slf4j.LoggerFactory
import java.util.logging.Logger

object GrpcExceptionHandlerInterceptor : ServerInterceptor {

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>, // 서버가 반환한 응답 메시지를 받을 객체
        headers: Metadata, // 통신 메타데이터를 가지는 객체로, 커스터메이징 가능
        next: ServerCallHandler<ReqT, RespT> // 인터셉터 체인의 다음 처리를 수행할 객체
    ): ServerCall.Listener<ReqT> { // 클라이언트로부터 전송된 수신 메시지를 처리할 객체
        val logger = LoggerFactory.getLogger(this::class.java)

        logger.info("[Server Interceptor] Invoke RPC - ${call.methodDescriptor.fullMethodName}")

        return next.startCall(
            // io.grpc.ServerCall의 경우 모든 메소드를 오버라이딩해줘야 함
            // 대신 SimpleForwardingServerCall 사용
            object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
                override fun close(status: Status, trailers: Metadata) {
                    val cause = status.cause
                    val rpcStatusCode = when (cause) {
                        is IllegalArgumentException -> Code.INVALID_ARGUMENT
                        is IllegalStateException -> Code.FAILED_PRECONDITION
                        is RuntimeException -> Code.INTERNAL
                        else -> Code.UNKNOWN
                    }

                    if (status.isOk || rpcStatusCode == Code.UNKNOWN) {
                        return super.close(status, trailers)
                    }

                    // 에러 로그 출력
                    logger.error(cause?.stackTraceToString())

                    /* error details 없이 코드만 반환
                    val translatedStatus = when (cause) {
                        is IllegalArgumentException -> Status.INVALID_ARGUMENT
                        is IllegalStateException -> Status.FAILED_PRECONDITION
                        else -> Status.UNKNOWN
                    }
                    val newStatus = translatedStatus.withDescription(cause?.message).withCause(cause)
                    return super.close(newStatus, trailers)
                    */

                    /* error details 함께 반환 */
                    val rpcStatus = com.google.rpc.Status.newBuilder()
                        .setCode(rpcStatusCode.number)
                        .addDetails(
                            com.google.protobuf.Any.pack(
                                ErrorInfo.newBuilder()
                                    .setReason(cause?.message)
                                    .build()
                            )
                        ).build()
                    val statusRuntimeException = StatusProto.toStatusRuntimeException(rpcStatus)

                    return super.close(
                        Status.fromThrowable(statusRuntimeException),
                        statusRuntimeException.trailers
                    )
                }
            },
            headers
        )
    }
}