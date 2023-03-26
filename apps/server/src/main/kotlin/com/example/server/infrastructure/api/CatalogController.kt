package com.example.server.infrastructure.api

import com.example.libs.domain.Catalog
import com.example.server.infrastructure.persistence.CatalogRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.CommonsRequestLoggingFilter

@RestController
class CatalogController(
    private val catalogRepository: CatalogRepository
) {
    @PostMapping("/catalogs")
    fun catalogs(): List<Catalog> = catalogRepository.findAll().map {
        Catalog(
            id = it.id,
            name = it.name,
            content = it.content,
            updateDate = it.updateDate,
            updateId = it.updateId
        )
    }
}

@Configuration
class RequestLoggingFilterConfig {
    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(false)
        return filter
    }
}