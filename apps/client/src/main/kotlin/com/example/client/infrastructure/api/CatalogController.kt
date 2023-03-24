package com.example.client.infrastructure.api

import com.example.client.application.CatalogService
import com.example.libs.domain.Catalog
import org.springframework.web.bind.annotation.*

@RestController
class CatalogController(
    private val catalogService: CatalogService
) {

    @GetMapping("/catalogs")
    fun catalogs() = catalogService.catalogs()

    @GetMapping("/catalogs/{id}")
    fun catalogs(@PathVariable id: Long) = catalogService.catalogs(id)

    @GetMapping("/catalogs-async")
    suspend fun catalogsByAsync() = catalogService.catalogsByAsync()

    @GetMapping("/catalogs-async/{id}")
    suspend fun catalogsByAsync(@PathVariable id: Long?) = catalogService.catalogsByAsync(id)

    @PutMapping("/catalogs")
    fun catalog(@RequestBody catalog: Catalog) = catalogService.create(catalog)

    @GetMapping("/catalogs-api")
    fun catalogsByApi() = catalogService.catalogsByApi()

}