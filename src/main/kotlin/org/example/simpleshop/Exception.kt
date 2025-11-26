package org.example.simpleshop

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.Locale

@ControllerAdvice
class ExceptionHandler(
    private val errorMessageSource: ResourceBundleMessageSource,
) {
    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(exception: Throwable): ResponseEntity<Any> {
        when (exception) {
            is ShopAppException-> {

                return ResponseEntity
                    .badRequest()
                    .body(exception.getErrorMessage(errorMessageSource))
            }

            else -> {
                exception.printStackTrace()
                return ResponseEntity
                    .badRequest().body(
                        BaseMessage(100,
                            "Iltimos support bilan bog'laning")
                    )
            }
        }
    }

}



sealed class ShopAppException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    protected open fun getErrorMessageArguments(): Array<out Any> = emptyArray()
    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
                getErrorMessageArguments(),
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}

data class BaseMessage(
    val code: Int,
    val message: String,
)

enum class ErrorCode(val code: Int) {
    CATEGORY_NOT_FOUND(1001),
    CATEGORY_ALREADY_EXISTS(1002),
    CATEGORY_IN_USE(1003),


    PRODUCT_NOT_FOUND(1101),
    PRODUCT_ALREADY_EXISTS(1102),
    INSUFFICIENT_PRODUCT_COUNT(1103),
    INVALID_PRODUCT_COUNT(1104),

    // User Exceptions (1200-1299)
    USER_NOT_FOUND(1201),
    USER_ALREADY_EXISTS(1202),
    INVALID_USERNAME(1203),
    INSUFFICIENT_USER_BALANCE(1204),
    USER_NOT_ADMIN(1205),

    TRANSACTION_NOT_FOUND(1301),
    INVALID_TRANSACTION_AMOUNT(1302),
    TRANSACTION_ITEM_NOT_FOUND(1303),
    INVALID_TRANSACTION_DATE(1304),

    PAYMENT_TRANSACTION_NOT_FOUND(1401),
    INVALID_PAYMENT_AMOUNT(1402),
    PAYMENT_FAILED(1403),
    INVALID_PAYMENT_DATE(1404),

    INVALID_INPUT(1501),
    OPERATION_FORBIDDEN(1502),
    INTERNAL_SERVER_ERROR(1503),
}

class CategoryNotFoundException : ShopAppException() {
    override fun errorType() = ErrorCode.CATEGORY_NOT_FOUND
}

class CategoryAlreadyExistsException(
    private val categoryName: String,
) : ShopAppException("Category with name: $categoryName already exists") {
    override fun errorType() = ErrorCode.CATEGORY_ALREADY_EXISTS
    override fun getErrorMessageArguments() = arrayOf(categoryName)
}

class CategoryInUseException(
    private val categoryName: String,
) : ShopAppException("Category: $categoryName is still in use") {
    override fun errorType() = ErrorCode.CATEGORY_IN_USE
    override fun getErrorMessageArguments() = arrayOf(categoryName)
}

class ProductNotFoundException : ShopAppException() {
    override fun errorType() = ErrorCode.PRODUCT_NOT_FOUND
}

class ProductAlreadyExistsException(
    private val productName: String,
) : ShopAppException("Product with name: $productName already exists") {
    override fun errorType() = ErrorCode.PRODUCT_ALREADY_EXISTS
    override fun getErrorMessageArguments() = arrayOf(productName)
}

class InsufficientProductCountException(
    private val productName: String,
    private val requested: Long,
    private val available: Long,
) : ShopAppException("Product: $productName requested: $requested, available: $available") {
    override fun errorType() = ErrorCode.INSUFFICIENT_PRODUCT_COUNT
    override fun getErrorMessageArguments() = arrayOf(productName, requested, available)
}

class InvalidProductCountException(
    private val count: Long,
) : ShopAppException("Invalid product count: $count") {
    override fun errorType() = ErrorCode.INVALID_PRODUCT_COUNT
    override fun getErrorMessageArguments() = arrayOf(count)
}

class UserNotFoundException : ShopAppException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class UserAlreadyExistsException(
    private val username: String,
) : ShopAppException("User with username: $username already exists") {
    override fun errorType() = ErrorCode.USER_ALREADY_EXISTS
    override fun getErrorMessageArguments() = arrayOf(username)
}

class InvalidUsernameException(
    private val username: String,
) : ShopAppException("Invalid username: $username") {
    override fun errorType() = ErrorCode.INVALID_USERNAME
    override fun getErrorMessageArguments() = arrayOf(username)
}

class InsufficientUserBalanceException(
    private val username: String,
    private val required: java.math.BigDecimal,
    private val available: java.math.BigDecimal,
) : ShopAppException("User: $username required: $required, available: $available") {
    override fun errorType() = ErrorCode.INSUFFICIENT_USER_BALANCE
    override fun getErrorMessageArguments() = arrayOf(username, required, available)
}

class UserNotAdminException(
    private val username: String,
) : ShopAppException("User: $username does not have admin privileges") {
    override fun errorType() = ErrorCode.USER_NOT_ADMIN
    override fun getErrorMessageArguments() = arrayOf(username)
}

class TransactionNotFoundException : ShopAppException() {
    override fun errorType() = ErrorCode.TRANSACTION_NOT_FOUND
}

class InvalidTransactionAmountException(
    private val amount: java.math.BigDecimal,
) : ShopAppException("Invalid transaction amount: $amount") {
    override fun errorType() = ErrorCode.INVALID_TRANSACTION_AMOUNT
    override fun getErrorMessageArguments() = arrayOf(amount)
}

class TransactionItemNotFoundException : ShopAppException() {
    override fun errorType() = ErrorCode.TRANSACTION_ITEM_NOT_FOUND
}

class InvalidTransactionDateException(
    private val date: String,
) : ShopAppException("Invalid transaction date: $date") {
    override fun errorType() = ErrorCode.INVALID_TRANSACTION_DATE
    override fun getErrorMessageArguments() = arrayOf(date)
}

class PaymentTransactionNotFoundException : ShopAppException() {
    override fun errorType() = ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND
}

class InvalidPaymentAmountException(
    private val amount: java.math.BigDecimal,
) : ShopAppException("Invalid payment amount: $amount") {
    override fun errorType() = ErrorCode.INVALID_PAYMENT_AMOUNT
    override fun getErrorMessageArguments() = arrayOf(amount)
}

class PaymentFailedException(
    private val userId: Long,
    private val amount: java.math.BigDecimal,
) : ShopAppException("Payment failed for user: $userId, amount: $amount") {
    override fun errorType() = ErrorCode.PAYMENT_FAILED
    override fun getErrorMessageArguments() = arrayOf(userId, amount)
}

class InvalidPaymentDateException(
    private val date: String,
) : ShopAppException("Invalid payment date: $date") {
    override fun errorType() = ErrorCode.INVALID_PAYMENT_DATE
    override fun getErrorMessageArguments() = arrayOf(date)
}

class InvalidInputException(
    private val fieldName: String,
    private val reason: String,
) : ShopAppException("Invalid input for field: $fieldName, reason: $reason") {
    override fun errorType() = ErrorCode.INVALID_INPUT
    override fun getErrorMessageArguments() = arrayOf(fieldName, reason)
}

class OperationForbiddenException(
    private val operation: String,
) : ShopAppException("Operation forbidden: $operation") {
    override fun errorType() = ErrorCode.OPERATION_FORBIDDEN
    override fun getErrorMessageArguments() = arrayOf(operation)
}

class InternalServerErrorException(
    override val message: String,
) : ShopAppException(message) {
    override fun errorType() = ErrorCode.INTERNAL_SERVER_ERROR
    override fun getErrorMessageArguments() = arrayOf(message)
}