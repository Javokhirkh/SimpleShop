package org.example.simpleshop.services

import org.example.simpleshop.PaymentTransactionNotFoundException
import org.example.simpleshop.User
import org.example.simpleshop.UserNotFoundException
import org.example.simpleshop.UserPaymentTransaction
import org.example.simpleshop.UserPaymentTransactionRepository
import org.example.simpleshop.UserRepository
import org.example.simpleshop.dtoes.UserPaymentTransactionRequestDto
import org.example.simpleshop.dtoes.UserPaymentTransactionResponseDto
import org.example.simpleshop.dtoes.UserResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

interface UserPaymentTransactionService {
    fun createPaymentTransaction(dto: UserPaymentTransactionRequestDto): UserPaymentTransactionResponseDto
    fun getPaymentTransactionById(id: Long): UserPaymentTransactionResponseDto
    fun getAllPaymentTransactions(): List<UserPaymentTransactionResponseDto>
    fun getAllPaymentTransactionsPaginated(pageable: Pageable): Page<UserPaymentTransactionResponseDto>
    fun getPaymentTransactionsByUserId(userId: Long): List<UserPaymentTransactionResponseDto>
    fun getPaymentTransactionsByUserIdPaginated(userId: Long, pageable: Pageable): Page<UserPaymentTransactionResponseDto>
    fun getPaymentTransactionsByDate(date: LocalDate): List<UserPaymentTransactionResponseDto>
    fun getPaymentTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): List<UserPaymentTransactionResponseDto>
    fun getPaymentTransactionsByDateRangePaginated(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<UserPaymentTransactionResponseDto>
    fun getTotalPaymentByUserId(userId: Long): java.math.BigDecimal
    fun updatePaymentTransaction(id: Long, dto: UserPaymentTransactionRequestDto): UserPaymentTransactionResponseDto
    fun deletePaymentTransaction(id: Long): Boolean
}

@Service
class UserPaymentTransactionServiceImpl(
    private val userPaymentTransactionRepository: UserPaymentTransactionRepository,
    private val userRepository: UserRepository,
) : UserPaymentTransactionService {

    override fun createPaymentTransaction(dto: UserPaymentTransactionRequestDto): UserPaymentTransactionResponseDto {
        val user = userRepository.findByIdAndDeletedFalse(dto.userId)
            ?: throw UserNotFoundException()
        return userPaymentTransactionRepository.save(dto.toEntity(user)).toPaymentTransactionResponse()
    }

    override fun getPaymentTransactionById(id: Long): UserPaymentTransactionResponseDto =
        userPaymentTransactionRepository.findByIdAndDeletedFalse(id)
            ?.toPaymentTransactionResponse()
            ?: throw PaymentTransactionNotFoundException()

    override fun getAllPaymentTransactions(): List<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findAllNotDeleted().map { it.toPaymentTransactionResponse() }

    override fun getAllPaymentTransactionsPaginated(pageable: Pageable): Page<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findAllNotDeleted(pageable).map { it.toPaymentTransactionResponse() }

    override fun getPaymentTransactionsByUserId(userId: Long): List<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findByUserIdAndDeletedFalse(userId).map { it.toPaymentTransactionResponse() }

    override fun getPaymentTransactionsByUserIdPaginated(userId: Long, pageable: Pageable): Page<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findByUserIdAndDeletedFalse(userId, pageable).map { it.toPaymentTransactionResponse() }

    override fun getPaymentTransactionsByDate(date: LocalDate): List<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findByDateAndDeletedFalse(date).map { it.toPaymentTransactionResponse() }

    override fun getPaymentTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): List<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findByDateBetweenAndDeletedFalse(startDate, endDate).map { it.toPaymentTransactionResponse() }

    override fun getPaymentTransactionsByDateRangePaginated(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<UserPaymentTransactionResponseDto> =
        userPaymentTransactionRepository.findByDateBetweenAndDeletedFalse(startDate, endDate, pageable).map { it.toPaymentTransactionResponse() }

    override fun getTotalPaymentByUserId(userId: Long): java.math.BigDecimal =
        userPaymentTransactionRepository.findByUserIdAndDeletedFalse(userId)
            .fold(java.math.BigDecimal.ZERO) { acc, payment -> acc + payment.amount }

    override fun updatePaymentTransaction(id: Long, dto: UserPaymentTransactionRequestDto): UserPaymentTransactionResponseDto {
        val paymentTransaction = userPaymentTransactionRepository.findByIdAndDeletedFalse(id)
            ?: throw PaymentTransactionNotFoundException()
        val user = userRepository.findByIdAndDeletedFalse(dto.userId)
            ?: throw UserNotFoundException()

        paymentTransaction.user = user
        paymentTransaction.amount = dto.amount
        paymentTransaction.date = dto.date

        return userPaymentTransactionRepository.save(paymentTransaction).toPaymentTransactionResponse()
    }

    override fun deletePaymentTransaction(id: Long): Boolean =
        userPaymentTransactionRepository.trash(id) != null

    private fun UserPaymentTransactionRequestDto.toEntity(user: User) = UserPaymentTransaction(
        user = user,
        amount = this.amount,
        date = this.date,
    )

    private fun UserPaymentTransaction.toPaymentTransactionResponse() = UserPaymentTransactionResponseDto(
        id = requireNotNull(this.id) { "UserPaymentTransaction id cannot be null" },
        user = this.user.toUserResponse(),
        amount = this.amount,
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