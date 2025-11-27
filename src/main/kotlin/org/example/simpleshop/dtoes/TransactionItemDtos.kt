package org.example.simpleshop.dtoes

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.example.simpleshop.Product
import org.example.simpleshop.Transaction
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionItemRequest(
    @get:NotBlank(message = "INVALID_USER_ID")val productId: Long,
    @get:Min(1, message = "INVALID_QUANTITY")val count: Long,
    @get:Min(0, message = "INVALID_PRODUCT_PRICE")val amount: BigDecimal
)

data class TransactionItemResponse(
    val id: Long,
    val product: Product,
    val quantity: Long,
    val transaction: Transaction,
    val pricePerUnit: BigDecimal,
    val totalPrice: BigDecimal,
    val createdAt: LocalDateTime?,
)
