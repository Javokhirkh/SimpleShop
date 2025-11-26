package org.example.simpleshop.dtoes

import java.time.LocalDateTime

data class CategoryRequestDto(
    val name: String,
    val order: Long,
    val description: String,
)

data class CategoryResponseDto(
    val id: Long,
    val name: String,
    val order: Long,
    val description: String,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)
