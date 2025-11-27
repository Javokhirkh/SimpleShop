package org.example.simpleshop.dtoes

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CategoryRequestDto(
    @get:NotBlank(message = "INVALID_VALUE")val name: String,
    val order: Long,
    val description: String?,
)

data class CategoryFullResponse(
    val id: Long,
    val name: String,
    val order: Long?,
    val description: String?,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)
data class CategoryShortResponse(
    val id: Long,
    val name: String,
    val order: Long?,
    val description: String?
)
data class CategoryUpdateRequestDto(
    val name: String?,
    val order: Long?,
    var description: String?,
)
