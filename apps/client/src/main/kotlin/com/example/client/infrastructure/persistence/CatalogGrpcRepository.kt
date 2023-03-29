package com.example.client.infrastructure.persistence

import brave.Tracing
import brave.grpc.GrpcTracing
import com.example.client.ClientProperties
import com.example.libs.domain.Catalog
import com.example.libs.grpc.CatalogServiceGrpc
import com.example.libs.grpc.CatalogServiceGrpcKt
import com.example.libs.grpc.catalogSearchRequest
import io.grpc.ManagedChannelBuilder
import org.springframework.cloud.sleuth.brave.instrument.grpc.TracingManagedChannelBuilderCustomizer
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class CatalogGrpcRepository(
    clientProperties: ClientProperties,
    tracing: Tracing
) {
    private lateinit var stub: CatalogServiceGrpc.CatalogServiceBlockingStub
    private lateinit var asyncStub: CatalogServiceGrpcKt.CatalogServiceCoroutineStub

    init {
        val builder = ManagedChannelBuilder
            .forAddress(clientProperties.host, clientProperties.port)
            .intercept(GrpcHeaderInterceptor())
            .usePlaintext()

        TracingManagedChannelBuilderCustomizer(
            GrpcTracing.create(tracing)
        ).customize(builder)

        val channel = builder.build()

        stub = CatalogServiceGrpc.newBlockingStub(channel)
        asyncStub = CatalogServiceGrpcKt.CatalogServiceCoroutineStub(channel)
    }

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
        }
        val response = stub.createCatalog(request)

        return response.value
    }

}