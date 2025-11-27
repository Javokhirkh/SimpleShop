package org.example.simpleshop.dtoes

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class ProductRequest(
    @get:NotBlank(message = "INVALID_PRODUCT_NAME")val name: String,
    val count: Long,
    val categoryId: Long,
)

data class ProductFullResponse(
    val id: Long,
    val name: String,
    val count: Long,
    val category: CategoryFullResponse,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)
data class ProductShortResponse(
    val id: Long,
    val name: String,
    val count: Long,
    val category: CategoryFullResponse
)
data class ProductUpdateRequest(
    val name: String?,
    val count: Long?,
    val categoryId: Long?,
)