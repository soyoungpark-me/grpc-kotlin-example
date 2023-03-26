package com.example.server.infrastructure.api

import com.example.libs.grpc.CatalogSearchReply
import com.example.libs.grpc.CatalogSearchRequest
import com.example.libs.grpc.CatalogServiceGrpcKt
import com.example.libs.grpc.catalogSearchReply
import com.example.server.ServerProperties
import com.example.server.infrastructure.persistence.Catalog
import com.example.server.infrastructure.persistence.CatalogRepository
import com.google.protobuf.Int64Value
import io.grpc.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

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

        override suspend fun getCatalogs(request: CatalogSearchRequest): CatalogSearchReply {
            val response: List<Catalog> = if (request.hasId()) {
                catalogRepository.findByIdOrNull(request.id)?.let {
                    listOf(it)
                } ?: throw IllegalArgumentException("Catalog not found. id = ${request.id}")
            } else {
                catalogRepository.findAll().toList()
            }

            return catalogSearchReply {
                total = response.size.toLong()
                response.map { catalog ->
                    this.catalogs.add(
                        com.example.libs.grpc.catalog {
                            id = catalog.id!!
                            name = catalog.name!!
                            content = catalog.content!!
                            catalog.updateDate?.let { updateDate = it.toString() }
                            catalog.updateId?.let { updateId = it }
                        }
                    )
                }
            }
        }

        override suspend fun createCatalog(request: com.example.libs.grpc.Catalog): Int64Value {
            return Int64Value.of(
                catalogRepository.save(
                    Catalog().apply {
                        id = request.id
                        name = request.name
                        content = request.content
                        updateDate = request.updateDate?.takeIf { !it.isNullOrBlank() }
                            ?.let { OffsetDateTime.parse(it) }
                            ?: OffsetDateTime.now()
                        updateId = request.updateId
                    }
                ).id!!
            )
        }
    }
}