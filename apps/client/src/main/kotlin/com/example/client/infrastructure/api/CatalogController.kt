package com.example.client.infrastructure.api

import com.example.client.application.CatalogService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CatalogController(
    private val catalogService: CatalogService
) {

    @PostMapping("/catalogs")
    fun catalogs() = catalogService.catalogs()

    @PostMapping("/catalogs-async")
    suspend fun catalogsByAsync() = catalogService.catalogsByAsync()

    @PostMapping("/catalogs-api")
    fun catalogsByApi() = catalogService.catalogsByApi()

}