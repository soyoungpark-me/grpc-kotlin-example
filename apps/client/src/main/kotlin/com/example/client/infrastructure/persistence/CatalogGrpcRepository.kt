package com.example.client.infrastructure.persistence

import com.example.CatalogServiceGrpc
import com.example.libs.domain.Catalog
import com.example.CatalogServiceGrpcKt
import com.example.catalogRequest
import com.example.client.ClientProperties
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Repository

@Repository
class CatalogGrpcRepository(
    clientProperties: ClientProperties
) {
    private val channel = ManagedChannelBuilder.forTarget("${clientProperties.host}:${clientProperties.port}")
        .usePlaintext()
        .build()

    private val stub = CatalogServiceGrpc.newBlockingStub(channel)
    private val asyncStub = CatalogServiceGrpcKt.CatalogServiceCoroutineStub(channel)

    fun catalogs(): List<Catalog> {
        val request = catalogRequest { }
        val response = stub.catalog(request)

        return response.catalogsList.toList().map {
            Catalog(
                id = it.id,
                name = it.name
            )
        }
    }

    suspend fun catalogsByAsync(): List<Catalog> {
        val request = catalogRequest { }
        val response = asyncStub.catalog(request)

        return response.catalogsList.toList().map {
            Catalog(
                id = it.id,
                name = it.name
            )
        }
    }

}