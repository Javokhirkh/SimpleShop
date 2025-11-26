package org.example.simpleshop.dtoes

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class UserPaymentTransactionRequestDto(
    val userId: Long,
    val amount: BigDecimal,
    val date: LocalDate,
)

data class UserPaymentTransactionResponseDto(
    val id: Long,
    val user: UserResponseDto,
    val amount: BigDecimal,
    val date: LocalDate,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)