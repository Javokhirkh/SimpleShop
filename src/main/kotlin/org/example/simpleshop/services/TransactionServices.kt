package org.example.simpleshop.services

import org.example.simpleshop.Transaction
import org.example.simpleshop.TransactionNotFoundException
import org.example.simpleshop.TransactionRepository
import org.example.simpleshop.User
import org.example.simpleshop.UserNotAdminException
import org.example.simpleshop.UserNotFoundException
import org.example.simpleshop.UserRepository
import org.example.simpleshop.UserRole
import org.example.simpleshop.dtoes.TransactionRequestDto
import org.example.simpleshop.dtoes.TransactionResponseDto
import org.example.simpleshop.dtoes.UserResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

interface TransactionService {
    fun createTransaction(dto: TransactionRequestDto): TransactionResponseDto
    fun getTransactionById(id: Long): TransactionResponseDto
    fun getAllTransactions(role: UserRole): List<TransactionResponseDto>
    fun getAllTransactionsPaginated(pageable: Pageable): Page<TransactionResponseDto>
    fun getTransactionsByUserId(userId: Long): List<TransactionResponseDto>
    fun getTransactionsByUserIdPaginated(userId: Long, pageable: Pageable): Page<TransactionResponseDto>
    fun getTransactionsByDate(date: LocalDate): List<TransactionResponseDto>
    fun getTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): List<TransactionResponseDto>
    fun getTransactionsByDateRangePaginated(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<TransactionResponseDto>
    fun updateTransaction(id: Long, dto: TransactionRequestDto): TransactionResponseDto
    fun deleteTransaction(id: Long): Boolean
}

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
) : TransactionService {

    override fun createTransaction(dto: TransactionRequestDto): TransactionResponseDto {
        val user = userRepository.findByIdAndDeletedFalse(dto.userId)
            ?: throw UserNotFoundException()
        return transactionRepository.save(dto.toEntity(user)).toTransactionResponse()
    }

    override fun getTransactionById(id: Long): TransactionResponseDto =
        transactionRepository.findByIdAndDeletedFalse(id)
            ?.toTransactionResponse()
            ?: throw TransactionNotFoundException()

    override fun getAllTransactions(role : UserRole): List<TransactionResponseDto> {
        if(role == UserRole.USER){
            throw UserNotAdminException("Only admin can access all transactions")
        }
        return transactionRepository.findAllNotDeleted().map { it.toTransactionResponse() }

    }

    override fun getAllTransactionsPaginated(pageable: Pageable): Page<TransactionResponseDto> =
        transactionRepository.findAllNotDeleted(pageable).map { it.toTransactionResponse() }

    override fun getTransactionsByUserId(userId: Long): List<TransactionResponseDto> =
        transactionRepository.findByUserIdAndDeletedFalse(userId).map { it.toTransactionResponse() }

    override fun getTransactionsByUserIdPaginated(userId: Long, pageable: Pageable): Page<TransactionResponseDto> =
        transactionRepository.findByUserIdAndDeletedFalse(userId, pageable).map { it.toTransactionResponse() }

    override fun getTransactionsByDate(date: LocalDate): List<TransactionResponseDto> =
        transactionRepository.findByDateAndDeletedFalse(date).map { it.toTransactionResponse() }

    override fun getTransactionsByDateRange(startDate: LocalDate, endDate: LocalDate): List<TransactionResponseDto> =
        transactionRepository.findByDateBetweenAndDeletedFalse(startDate, endDate).map { it.toTransactionResponse() }

    override fun getTransactionsByDateRangePaginated(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<TransactionResponseDto> =
        transactionRepository.findByDateBetweenAndDeletedFalse(startDate, endDate, pageable).map { it.toTransactionResponse() }

    override fun updateTransaction(id: Long, dto: TransactionRequestDto): TransactionResponseDto {
        val transaction = transactionRepository.findByIdAndDeletedFalse(id)
            ?: throw TransactionNotFoundException()
        val user = userRepository.findByIdAndDeletedFalse(dto.userId)
            ?: throw UserNotFoundException()

        transaction.user = user
        transaction.totalAmount = dto.totalAmount
        transaction.date = dto.date

        return transactionRepository.save(transaction).toTransactionResponse()
    }

    override fun deleteTransaction(id: Long): Boolean =
        transactionRepository.trash(id) != null

    private fun TransactionRequestDto.toEntity(user: User) = Transaction(
        user = user,
        totalAmount = this.totalAmount,
        date = this.date,
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