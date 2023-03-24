package com.example.server.infrastructure.api

import com.example.libs.domain.Catalog
import com.example.server.infrastructure.persistence.CatalogRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

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

