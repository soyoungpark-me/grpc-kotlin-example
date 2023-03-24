package com.example.client.application

import com.example.client.infrastructure.persistence.CatalogApiRepository
import com.example.client.infrastructure.persistence.CatalogGrpcRepository
import com.example.libs.domain.Catalog
import org.springframework.stereotype.Service

@Service
class CatalogService(
    private val catalogGrpcRepository: CatalogGrpcRepository,
    private val catalogApiRepository: CatalogApiRepository
) {
    fun catalogs(id: Long? = null) = catalogGrpcRepository.catalogs(id)

    suspend fun catalogsByAsync(id: Long? = null) = catalogGrpcRepository.catalogsByAsync(id)

    fun create(catalog: Catalog) = catalogGrpcRepository.save(catalog)

    fun catalogsByApi() = catalogApiRepository.catalogs()
}