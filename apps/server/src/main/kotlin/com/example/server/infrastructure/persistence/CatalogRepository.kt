package com.example.server.infrastructure.persistence

import com.example.libs.domain.Catalog
import org.springframework.stereotype.Repository

@Repository
class CatalogRepository {
    fun catalogs(): List<Catalog> {
        return (1..1000).map {
            Catalog(id = it.toLong(), name = "test_${it}")
        }
    }
}