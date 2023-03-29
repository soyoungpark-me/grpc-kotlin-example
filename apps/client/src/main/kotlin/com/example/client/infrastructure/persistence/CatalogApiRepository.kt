package com.example.client.infrastructure.persistence

import com.example.client.application.AuditService
import com.example.libs.domain.Catalog
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
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

@Service
class AuditInterceptor(
    private val auditService: AuditService
): RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        template.header("X-USER-ID", auditService.id)
    }
}