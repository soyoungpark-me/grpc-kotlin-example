package com.example.server.infrastructure.api

import com.example.server.infrastructure.persistence.CatalogRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CatalogController(
    private val catalogRepository: CatalogRepository
) {
    @PostMapping("/catalogs")
    fun catalogs() = catalogRepository.catalogs()
}

