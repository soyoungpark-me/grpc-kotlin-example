package com.example.libs.domain

import java.time.OffsetDateTime

data class Catalog(
    val id: Long? = null,
    val name: String? = null,
    val content: String? = null,
    val updateDate: OffsetDateTime? = null,
    val updateId: String? = null
)