package com.example.client.infrastructure.persistence

import com.example.libs.domain.Catalog
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.PostMapping

@Repository
class CatalogApiRepository(
    private val catalogApiFeignClient: CatalogApiFeignClient,
) {
    fun catalogs() = catalogApiFeignClient.catalogs()
}

@FeignClient(qualifier = "catalogApiClient", url = "\${feign.client.config.catalogApi.url}", name = "catalogApi")
interface CatalogApiFeignClient {
    @PostMapping("/catalogs")
    fun catalogs(): List<Catalog>
}