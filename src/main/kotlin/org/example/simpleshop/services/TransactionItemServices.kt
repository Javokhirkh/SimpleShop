package org.example.simpleshop.services

import org.example.simpleshop.Product
import org.example.simpleshop.ProductNotFoundException
import org.example.simpleshop.ProductRepository
import org.example.simpleshop.Transaction
import org.example.simpleshop.TransactionItem
import org.example.simpleshop.TransactionItemMapper
import org.example.simpleshop.TransactionItemRepository
import org.example.simpleshop.dtoes.TransactionItemRequest
import org.example.simpleshop.dtoes.TransactionItemResponse
import org.springframework.stereotype.Service

interface TransactionItemService {
    fun createALlItems(item:List<TransactionItemRequest>,transaction: Transaction)
    fun getItemsByTransactionId(transactionId: Long): List<TransactionItemResponse>
}

@Service
class TransactionItemServiceImpl(
    private val repository: TransactionItemRepository,
    private val mapper: TransactionItemMapper,
    private val productRepository: ProductRepository
) : TransactionItemService {
    override fun createALlItems(item: List<TransactionItemRequest>, transaction: Transaction) {
        val transactionItems = item.map { dto ->
            val product: Product = productRepository.findByIdAndDeletedFalse(dto.productId)
                ?: throw ProductNotFoundException()
            val totalAmount = dto.amount * dto.count.toBigDecimal()
            mapper.toEntity(dto,product , transaction,totalAmount)
        }
        repository.saveAll(transactionItems)
    }

    override fun getItemsByTransactionId(transactionId: Long): List<TransactionItemResponse> {
        val items = repository.findAllByTransactionIdAndDeletedFalse(transactionId)
        return items.map { mapper.toResponse(it) }
    }


}
