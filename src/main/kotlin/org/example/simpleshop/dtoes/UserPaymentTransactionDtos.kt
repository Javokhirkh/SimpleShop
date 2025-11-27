package org.example.simpleshop.dtoes

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.time.LocalDateTime

data class UserPaymentTransactionRequestDto(
    @get:NotBlank(message = "user id cannot be empty")val userId: Long,
    @get:Min(1, message = "balance cannot be lower than 1")val amount: BigDecimal,
)

data class UserPaymentTransactionResponseFullInformation(
    val id: Long,
    val user: UserShortResponse,
    val amount: BigDecimal,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)
data class UserPaymentTransactionShortResponse(
    val id: Long,
    val amount: BigDecimal,
    val user: UserShortResponse
)