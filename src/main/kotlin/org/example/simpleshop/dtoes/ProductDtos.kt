package org.example.simpleshop.dtoes

import java.time.LocalDateTime

data class ProductRequestDto(
    val name: String,
    val count: Long,
    val categoryId: Long,
)

data class ProductResponseDto(
    val id: Long,
    val name: String,
    val count: Long,
    val category: CategoryResponseDto,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)