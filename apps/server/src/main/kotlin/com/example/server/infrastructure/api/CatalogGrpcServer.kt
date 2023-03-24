package com.example.server.infrastructure.api

import com.example.libs.grpc.CatalogReply
import com.example.libs.grpc.CatalogRequest
import com.example.libs.grpc.CatalogServiceGrpcKt
import com.example.libs.grpc.catalogReply
import com.example.server.ServerProperties
import com.example.server.infrastructure.persistence.Catalog
import com.example.server.infrastructure.persistence.CatalogRepository
import io.grpc.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CatalogGrpcServer(
    serverProperties: ServerProperties,
    catalogRepository: CatalogRepository
) {
    val server: Server = ServerBuilder
        .forPort(serverProperties.port)
        .addService(
            // Exception 인터셉터 등록
            // CatalogGrpcService 코드가 실행되기 전에 인터셉터에 구현된 내용이 먼저 수행됨
            ServerInterceptors.intercept(
                CatalogGrpcService(catalogRepository), GrpcExceptionHandlerInterceptor
            )
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
        private val catalogRepository: CatalogRepository
    ) : CatalogServiceGrpcKt.CatalogServiceCoroutineImplBase() {

        override suspend fun catalogs(request: CatalogRequest): CatalogReply {
            val response: List<Catalog> = if (request.hasId()) {
                catalogRepository.findByIdOrNull(request.id)?.let {
                    listOf(it)
                } ?: throw IllegalArgumentException("Catalog not found. id = ${request.id}")
            } else {
                catalogRepository.findAll().toList()
            }

            return catalogReply {
                total = response.size.toLong()
                response.map {
                    this.catalogs.add(
                        com.example.libs.grpc.catalog {
                            id = it.id!!
                            name = it.name!!
                            content = it.content!!
                            updateDate = it.updateDate!!.toString()
                            updateId = it.updateId!!
                        }
                    )
                }
            }
        }
    }
}