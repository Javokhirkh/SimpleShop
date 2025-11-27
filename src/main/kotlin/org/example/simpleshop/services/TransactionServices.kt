package org.example.simpleshop.services

import jakarta.transaction.Transactional
import org.example.simpleshop.Transaction
import org.example.simpleshop.TransactionMapper
import org.example.simpleshop.TransactionRepository
import org.example.simpleshop.User
import org.example.simpleshop.UserNotAdminException
import org.example.simpleshop.UserNotFoundException
import org.example.simpleshop.UserRepository
import org.example.simpleshop.UserRole
import org.example.simpleshop.dtoes.TransactionFullResponse
import org.example.simpleshop.dtoes.TransactionItemRequest
import org.example.simpleshop.dtoes.TransactionRequestDto
import org.example.simpleshop.dtoes.TransactionShortResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

interface TransactionService {
    fun createTransaction(dto: TransactionRequestDto)
    fun getTransactionById(id: Long): TransactionFullResponse
    fun getAllTransactionsPaginated(pageable: Pageable): Page<TransactionShortResponse>
    fun getTransactionsByUserIdPaginated(userId: Long, pageable: Pageable): Page<TransactionShortResponse>
    fun getTransactionsByDateRangePaginated(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<TransactionShortResponse>
    fun deleteTransaction(id: Long)
}

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val transactionItemService: TransactionItemService,
    private val mapper: TransactionMapper
) : TransactionService {
    @Transactional
    override fun createTransaction(dto: TransactionRequestDto) {
        val user: User = userRepository.findByIdAndDeletedFalse(dto.userId)
            ?: throw UserNotFoundException()
        val amount=calculateTotalAmount(dto.items)
        val transaction = Transaction(
            user = user,
            totalAmount = amount
        )
        transactionRepository.save(transaction)
        transactionItemService.createALlItems(dto.items,transaction)

        transactionRepository.save(transaction)
    }

    override fun getTransactionById(id: Long): TransactionFullResponse {
        val transaction: Transaction = transactionRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()
        val items=transactionItemService.getItemsByTransactionId(transaction.id!!)
        val answer =mapper.toTransactionFullResponseDto(transaction,items)
        return answer
    }

    override fun getAllTransactionsPaginated(pageable: Pageable): Page<TransactionShortResponse> {
        val transactions = transactionRepository.findAllNotDeleted(pageable)
        return transactions.map { mapper.toTransactionShortResponseDto(it) }
    }

    override fun getTransactionsByUserIdPaginated(
        userId: Long,
        pageable: Pageable
    ): Page<TransactionShortResponse> {
        val user=userRepository.findByIdAndDeletedFalse(userId)
            ?:throw UserNotFoundException()
        if (user.userRole!= UserRole.ADMIN){
            throw UserNotAdminException(user.username)
        }
        val transactions = transactionRepository.findAllByUserIdAndDeletedFalse(userId,pageable)
        return transactions.map { mapper.toTransactionShortResponseDto(it) }
    }

    override fun getTransactionsByDateRangePaginated(
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<TransactionShortResponse> {
        val transactions = transactionRepository.findAllNotDeleted(pageable)
        return transactions.map { mapper.toTransactionShortResponseDto(it) }
    }

    @Transactional
    override fun deleteTransaction(id: Long) {
        transactionRepository.trash(id)
    }

    private fun calculateTotalAmount(items: List<TransactionItemRequest>): BigDecimal {
        var totalAmount = BigDecimal.ZERO
        for (item in items) {
            totalAmount += item.amount* BigDecimal(item.count)
        }
        return totalAmount
    }
}
