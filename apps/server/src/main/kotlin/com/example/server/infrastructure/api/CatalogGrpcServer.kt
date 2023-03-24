package com.example.server.infrastructure.api

import com.example.CatalogReply
import com.example.CatalogServiceGrpcKt
import com.example.catalogReply
import com.example.server.infrastructure.persistence.CatalogRepository
import io.grpc.*
import com.example.CatalogRequest as CatalogRequest1

class CatalogGrpcServer(private val port: Int) {
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
        override suspend fun catalog(request: CatalogRequest1): CatalogReply {
            val response = catalogRepository.catalogs()

            return catalogReply {
                total = response.size.toLong()
                response.map {
                    this.catalogs.add(
                        com.example.catalog {
                            id = it.id!!
                            name = it.name!!
                        }
                    )
                }
            }
        }
    }
}