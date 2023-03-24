package com.example.client.application

import com.example.client.infrastructure.persistence.CatalogApiRepository
import com.example.client.infrastructure.persistence.CatalogGrpcRepository
import org.springframework.stereotype.Service

@Service
class CatalogService(
    private val catalogGrpcRepository: CatalogGrpcRepository,
    private val catalogApiRepository: CatalogApiRepository
) {
    fun catalogs() = catalogGrpcRepository.catalogs()

    suspend fun catalogsByAsync() = catalogGrpcRepository.catalogsByAsync()

    fun catalogsByApi() = catalogApiRepository.catalogs()
}