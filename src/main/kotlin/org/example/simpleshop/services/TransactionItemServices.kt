package org.example.simpleshop.services

import org.example.simpleshop.Category
import org.example.simpleshop.Product
import org.example.simpleshop.ProductNotFoundException
import org.example.simpleshop.ProductRepository
import org.example.simpleshop.Transaction
import org.example.simpleshop.TransactionItem
import org.example.simpleshop.TransactionItemNotFoundException
import org.example.simpleshop.TransactionItemRepository
import org.example.simpleshop.TransactionNotFoundException
import org.example.simpleshop.TransactionRepository
import org.example.simpleshop.User
import org.example.simpleshop.dtoes.CategoryResponseDto
import org.example.simpleshop.dtoes.ProductResponseDto
import org.example.simpleshop.dtoes.TransactionItemRequestDto
import org.example.simpleshop.dtoes.TransactionItemResponseDto
import org.example.simpleshop.dtoes.TransactionResponseDto
import org.example.simpleshop.dtoes.UserResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface TransactionItemService {
    fun createTransactionItem(dto: TransactionItemRequestDto): TransactionItemResponseDto
    fun getTransactionItemById(id: Long): TransactionItemResponseDto
    fun getAllTransactionItems(): List<TransactionItemResponseDto>
    fun getAllTransactionItemsPaginated(pageable: Pageable): Page<TransactionItemResponseDto>
    fun getTransactionItemsByTransactionId(transactionId: Long): List<TransactionItemResponseDto>
    fun getTransactionItemsByProductId(productId: Long): List<TransactionItemResponseDto>
    fun getTransactionItemsByProductIdPaginated(productId: Long, pageable: Pageable): Page<TransactionItemResponseDto>
    fun updateTransactionItem(id: Long, dto: TransactionItemRequestDto): TransactionItemResponseDto
    fun deleteTransactionItem(id: Long): Boolean
}

@Service
class TransactionItemServiceImpl(
    private val transactionItemRepository: TransactionItemRepository,
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository,
) : TransactionItemService {

    override fun createTransactionItem(dto: TransactionItemRequestDto): TransactionItemResponseDto {
        val product = productRepository.findByIdAndDeletedFalse(dto.productId)
            ?: throw ProductNotFoundException()
        val transaction = transactionRepository.findByIdAndDeletedFalse(dto.transactionId)
            ?: throw TransactionNotFoundException()

        return transactionItemRepository.save(dto.toEntity(product, transaction)).toTransactionItemResponse()
    }

    override fun getTransactionItemById(id: Long): TransactionItemResponseDto =
        transactionItemRepository.findByIdAndDeletedFalse(id)
            ?.toTransactionItemResponse()
            ?: throw TransactionItemNotFoundException()

    override fun getAllTransactionItems(): List<TransactionItemResponseDto> =
        transactionItemRepository.findAllNotDeleted().map { it.toTransactionItemResponse() }

    override fun getAllTransactionItemsPaginated(pageable: Pageable): Page<TransactionItemResponseDto> =
        transactionItemRepository.findAllNotDeleted(pageable).map { it.toTransactionItemResponse() }

    override fun getTransactionItemsByTransactionId(transactionId: Long): List<TransactionItemResponseDto> =
        transactionItemRepository.findByTransactionIdAndDeletedFalse(transactionId).map { it.toTransactionItemResponse() }

    override fun getTransactionItemsByProductId(productId: Long): List<TransactionItemResponseDto> =
        transactionItemRepository.findByProductIdAndDeletedFalse(productId).map { it.toTransactionItemResponse() }

    override fun getTransactionItemsByProductIdPaginated(productId: Long, pageable: Pageable): Page<TransactionItemResponseDto> =
        transactionItemRepository.findByProductIdAndDeletedFalse(productId, pageable).map { it.toTransactionItemResponse() }

    override fun updateTransactionItem(id: Long, dto: TransactionItemRequestDto): TransactionItemResponseDto {
        val transactionItem = transactionItemRepository.findByIdAndDeletedFalse(id)
            ?: throw TransactionItemNotFoundException()
        val product = productRepository.findByIdAndDeletedFalse(dto.productId)
            ?: throw ProductNotFoundException()
        val transaction = transactionRepository.findByIdAndDeletedFalse(dto.transactionId)
            ?: throw TransactionNotFoundException()

        transactionItem.product = product
        transactionItem.count = dto.count
        transactionItem.amount = dto.amount
        transactionItem.totalAmount = dto.totalAmount
        transactionItem.transaction = transaction

        return transactionItemRepository.save(transactionItem).toTransactionItemResponse()
    }

    override fun deleteTransactionItem(id: Long): Boolean =
        transactionItemRepository.trash(id) != null

    private fun TransactionItemRequestDto.toEntity(product: Product, transaction: Transaction) = TransactionItem(
        product = product,
        count = this.count,
        amount = this.amount,
        totalAmount = this.totalAmount,
        transaction = transaction,
    )

    private fun TransactionItem.toTransactionItemResponse() = TransactionItemResponseDto(
        id = requireNotNull(this.id) { "TransactionItem id cannot be null" },
        product = this.product.toProductResponse(),
        count = this.count,
        amount = this.amount,
        totalAmount = this.totalAmount,
        transaction = this.transaction.toTransactionResponse(),
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )

    private fun Product.toProductResponse() = ProductResponseDto(
        id = requireNotNull(this.id) { "Product id cannot be null" },
        name = this.name,
        count = this.count,
        category = this.category.toCategoryResponse(),
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )

    private fun Category.toCategoryResponse() = CategoryResponseDto(
        id = requireNotNull(this.id) { "Category id cannot be null" },
        name = this.name,
        order = this.order,
        description = this.description,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )

    private fun Transaction.toTransactionResponse() = TransactionResponseDto(
        id = requireNotNull(this.id) { "Transaction id cannot be null" },
        user = this.user.toUserResponse(),
        totalAmount = this.totalAmount,
        date = this.date,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )

    private fun User.toUserResponse() = UserResponseDto(
        id = this.id,
        username = this.username,
        fullName = this.fullName,
        balance = this.balance,
        userRole = this.userRole,
        createdDate = this.createdDate,
        modifiedDate = this.modifiedDate,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
    )
}