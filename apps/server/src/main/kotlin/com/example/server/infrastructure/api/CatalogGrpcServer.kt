package com.example.server.infrastructure.api

import com.example.libs.grpc.CatalogReply
import com.example.libs.grpc.CatalogRequest
import com.example.libs.grpc.CatalogServiceGrpcKt
import com.example.libs.grpc.catalogReply
import com.example.server.infrastructure.persistence.CatalogRepository
import io.grpc.*

class CatalogGrpcServer(port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(
            // Exception 인터셉터 등록
            // CatalogGrpcService 코드가 실행되기 전에 인터셉터에 구현된 내용이 먼저 수행됨
            ServerInterceptors.intercept(CatalogGrpcService(), GrpcExceptionHandlerInterceptor)
        )
        .build()

    fun start() {
        server.start()
        Runtime.getRuntime().addShutdownHook(
            Thread { this@CatalogGrpcServer.stop() }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class CatalogGrpcService(
        private val catalogRepository: CatalogRepository = CatalogRepository()
    ) : CatalogServiceGrpcKt.CatalogServiceCoroutineImplBase() {
        override suspend fun catalogs(request: CatalogRequest): CatalogReply {
            val response = catalogRepository.catalogs()

            return catalogReply {
                total = response.size.toLong()
                response.map {
                    this.catalogs.add(
                        com.example.libs.grpc.catalog {
                            id = it.id!!
                            name = it.name!!
                            content = ""
                            updateDate = ""
                            updateId = ""
                        }
                    )
                }
            }
        }
    }
}