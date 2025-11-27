package org.example.simpleshop.dtoes

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class TransactionRequestDto(
    val userId: Long,
    val items:List<TransactionItemRequest>,
)

data class TransactionShortResponse(
    val id: Long,
    val user: UserShortResponse,
    val totalAmount: BigDecimal,
    val createdDate: LocalDateTime?,
)

data class TransactionFullResponse(
    val id: Long,
    val user: UserShortResponse,
    val totalAmount: BigDecimal,
    val createdDate: LocalDateTime?,
    val items:List<TransactionItemResponse>,
)