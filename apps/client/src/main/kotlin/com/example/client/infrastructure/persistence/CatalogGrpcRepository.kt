package com.example.client.infrastructure.persistence

import com.example.libs.domain.Catalog
import com.example.client.ClientProperties
import com.example.libs.grpc.CatalogServiceGrpc
import com.example.libs.grpc.CatalogServiceGrpcKt
import com.example.libs.grpc.catalogSearchRequest
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class CatalogGrpcRepository(
    clientProperties: ClientProperties
) {
    private val channel = ManagedChannelBuilder.forTarget("${clientProperties.host}:${clientProperties.port}")
        .usePlaintext()
        .build()

    private val stub = CatalogServiceGrpc.newBlockingStub(channel)
    private val asyncStub = CatalogServiceGrpcKt.CatalogServiceCoroutineStub(channel)

    fun catalogs(catalogId: Long?): List<Catalog> {
        val request = catalogSearchRequest {
            catalogId?.let { id = it }
        }
        val response = stub.getCatalogs(request)

        return response.catalogsList.toList().map { catalog ->
            Catalog(
                id = catalog.id,
                name = catalog.name,
                content = catalog.content,
                updateDate = catalog.updateDate?.takeIf { !it.isNullOrBlank() }?.let { OffsetDateTime.parse(it) },
                updateId = catalog.updateId
            )
        }
    }

    suspend fun catalogsByAsync(catalogId: Long?): List<Catalog> {
        val request = catalogSearchRequest {
            catalogId?.let { id = it }
        }
        val response = asyncStub.getCatalogs(request)

        return response.catalogsList.toList().map { catalog ->
            Catalog(
                id = catalog.id,
                name = catalog.name,
                content = catalog.content,
                updateDate = catalog.updateDate?.takeIf { !it.isNullOrBlank() }?.let { OffsetDateTime.parse(it) },
                updateId = catalog.updateId
            )
        }
    }

    fun save(catalog: Catalog): Long {
        val request = com.example.libs.grpc.catalog {
            id = catalog.id!!
            name = catalog.name!!
            content = catalog.content!!
            catalog.updateDate?.let { updateDate = it.toString() }
            catalog.updateId?.let { updateId = it }
        }
        val response = stub.createCatalog(request)

        return response.value
    }

}