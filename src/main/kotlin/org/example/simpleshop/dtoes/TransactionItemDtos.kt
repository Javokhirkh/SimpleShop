package org.example.simpleshop.dtoes

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionItemRequestDto(
    val productId: Long,
    val count: Long,
    val amount: BigDecimal,
    val totalAmount: BigDecimal,
    val transactionId: Long,
)

data class TransactionItemResponseDto(
    val id: Long,
    val product: ProductResponseDto,
    val count: Long,
    val amount: BigDecimal,
    val totalAmount: BigDecimal,
    val transaction: TransactionResponseDto,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)