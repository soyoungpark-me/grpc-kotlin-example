package com.example.server.infrastructure.api

import com.example.libs.grpc.CatalogSearchReply
import com.example.libs.grpc.CatalogSearchRequest
import com.example.libs.grpc.CatalogServiceGrpcKt
import com.example.libs.grpc.catalogSearchReply
import com.example.server.application.AuditService
import com.example.server.infrastructure.persistence.Catalog
import com.example.server.infrastructure.persistence.CatalogRepository
import com.google.protobuf.Int64Value
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.data.repository.findByIdOrNull
import java.time.OffsetDateTime

@GrpcService
class CatalogGrpcServer(
    private val auditService: AuditService,
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
                    updateDate = OffsetDateTime.now()
                    updateId = auditService.getAuditId()
                }
            ).id!!
        )
    }
}