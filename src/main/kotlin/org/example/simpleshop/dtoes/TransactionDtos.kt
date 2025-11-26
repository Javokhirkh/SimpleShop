package org.example.simpleshop.dtoes

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class TransactionRequestDto(
    val userId: Long,
    val totalAmount: BigDecimal,
    val date: LocalDate,
)

data class TransactionResponseDto(
    val id: Long,
    val user: UserResponseDto,
    val totalAmount: BigDecimal,
    val date: LocalDate,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)