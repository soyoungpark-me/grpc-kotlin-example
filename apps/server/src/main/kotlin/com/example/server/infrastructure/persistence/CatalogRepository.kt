package com.example.server.infrastructure.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import javax.persistence.*

@Repository
interface CatalogRepository: CrudRepository<Catalog, Long>

@Entity
@Table(name = "CATALOG")
class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var name: String? = null
    var content: String? = null

    @Column(name = "update_date")
    var updateDate: OffsetDateTime? = null

    @Column(name = "update_id")
    var updateId: String? = null
}