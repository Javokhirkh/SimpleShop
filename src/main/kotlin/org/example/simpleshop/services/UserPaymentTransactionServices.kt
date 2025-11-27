package org.example.simpleshop.services

import jakarta.transaction.Transactional
import org.example.simpleshop.PaymentTransactionNotFoundException
import org.example.simpleshop.User
import org.example.simpleshop.UserNotFoundException
import org.example.simpleshop.UserPaymentTransaction
import org.example.simpleshop.UserPaymentTransactionMapper
import org.example.simpleshop.UserPaymentTransactionRepository
import org.example.simpleshop.UserRepository
import org.example.simpleshop.dtoes.UserPaymentTransactionRequestDto
import org.example.simpleshop.dtoes.UserPaymentTransactionResponseFullInformation
import org.example.simpleshop.dtoes.UserPaymentTransactionShortResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

interface UserPaymentTransactionService {
    fun createPaymentTransaction(dto: UserPaymentTransactionRequestDto)
    fun getPaymentTransactionById(id: Long): UserPaymentTransactionResponseFullInformation
    fun getAllPaymentTransactionsPaginated(pageable: Pageable): Page<UserPaymentTransactionShortResponse>
    fun getPaymentTransactionsByUserIdPaginated(userId: Long, pageable: Pageable): Page<UserPaymentTransactionShortResponse>
    fun getPaymentTransactionsByDatePaginated(date: LocalDate,pageable:Pageable): Page<UserPaymentTransactionShortResponse>
    fun getPaymentTransactionsByDateRangePaginated(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<UserPaymentTransactionShortResponse>
    fun getTotalPaymentByUserId(userId: Long): BigDecimal
    fun deletePaymentTransaction(id: Long)
}

@Service
class UserPaymentTransactionServiceImpl(
    private val userPaymentTransactionRepository: UserPaymentTransactionRepository,
    private val userRepository: UserRepository,
    private val mapper: UserPaymentTransactionMapper
) : UserPaymentTransactionService {
    @Transactional
    override fun createPaymentTransaction(dto: UserPaymentTransactionRequestDto) {
        val user: User = userRepository.findByIdAndDeletedFalse(dto.userId)
            ?: throw UserNotFoundException()

        val paymentTransaction = mapper.toEntity(user, dto.amount)
        user.balance += dto.amount
        userRepository.save(user)
        userPaymentTransactionRepository.save(paymentTransaction)
    }

    override fun getPaymentTransactionById(id: Long): UserPaymentTransactionResponseFullInformation {
        val transaction: UserPaymentTransaction = userPaymentTransactionRepository.findByIdAndDeletedFalse(id)
            ?: throw PaymentTransactionNotFoundException()
        return mapper.toFullInformation(transaction)
    }

    override fun getAllPaymentTransactionsPaginated(pageable: Pageable): Page<UserPaymentTransactionShortResponse> {
        val transactions = userPaymentTransactionRepository.findAllNotDeleted(pageable)
        return transactions.map { mapper.toShortResponse(it) }
    }

    override fun getPaymentTransactionsByUserIdPaginated(
        userId: Long,
        pageable: Pageable
    ): Page<UserPaymentTransactionShortResponse> {
        val user=userRepository.findByIdAndDeletedFalse(userId)
            ?:throw UserNotFoundException()
        val transactions = userPaymentTransactionRepository.findAllByUserAndDeletedFalse(user, pageable)
        return transactions.map { mapper.toShortResponse(it) }
    }

    override fun getPaymentTransactionsByDatePaginated(
        date: LocalDate,
        pageable: Pageable
    ): Page<UserPaymentTransactionShortResponse> {
        val transactions = userPaymentTransactionRepository.findAllByDateAndDeletedFalse(date, pageable)
        return transactions.map { mapper.toShortResponse(it) }
    }

    override fun getPaymentTransactionsByDateRangePaginated(
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<UserPaymentTransactionShortResponse> {
        val transactions = userPaymentTransactionRepository.findAllByDateRangeAndDeletedFalse(startDate, endDate, pageable)
        return transactions.map { mapper.toShortResponse(it) }
    }

    override fun getTotalPaymentByUserId(userId: Long): BigDecimal {
        val user=userRepository.findByIdAndDeletedFalse(userId)
            ?:throw UserNotFoundException()
        val transactions = userPaymentTransactionRepository.findAllByUserAndDeletedFalse(user, Pageable.unpaged())
        return transactions.content.fold(BigDecimal.ZERO) { acc, transaction -> acc + transaction.amount }
    }
    @Transactional
    override fun deletePaymentTransaction(id: Long) {
        userPaymentTransactionRepository.trash(id) ?: throw PaymentTransactionNotFoundException()
    }


}