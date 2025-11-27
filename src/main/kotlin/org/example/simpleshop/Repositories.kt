package org.example.simpleshop

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import java.time.LocalDate

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
    fun findAllNotDeleted(pageable: Pageable): Page<T>
}

class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>,
    entityManager: EntityManager,
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

    private val isNotDeletedSpecification = Specification<T> { root, _, cb ->
        cb.equal(root.get<Boolean>("deleted"), false)
    }

    override fun findByIdAndDeletedFalse(id: Long): T? =
        findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? =
        findByIdOrNull(id)?.run {
            deleted = true
            save(this)
        }

    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)

    override fun findAllNotDeleted(pageable: Pageable): Page<T> =
        findAll(isNotDeletedSpecification, pageable)

    override fun trashList(ids: List<Long>): List<T?> = ids.map { trash(it) }
}

@Repository
interface CategoryRepository : BaseRepository<Category>{
    fun existsByName(name: String)
    fun findAllByOrderByNameAsc(pageable: Pageable):Page<Category>
    @Query("select c from Category c where c.name = :name and c.id != :id and c.deleted = false")
    fun existsByNameAndNotEqualId(@Param("name") name: String, @Param("id") id: Long): Category?
}

@Repository
interface ProductRepository : BaseRepository<Product> {
    @Query("select p from Product p where p.category = :category and p.deleted = false")
    fun findAllByCategoryAndDeletedFalse(@Param("category") category: Category, pageable: Pageable): Page<Product>
}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByUsername(username: String): User?
}

@Repository
interface TransactionRepository : BaseRepository<Transaction> {
    @Query("select t from Transaction t where t.user.id = :userId and t.deleted = false")
    fun findAllByUserIdAndDeletedFalse(@Param("userId") userId: Long, pageable: Pageable): Page<Transaction>
}

@Repository
interface TransactionItemRepository : BaseRepository<TransactionItem> {
    @Query("select ti from TransactionItem ti where ti.transaction.id = :transactionId and ti.deleted = false")
    fun findAllByTransactionIdAndDeletedFalse(@Param("transactionId") transactionId: Long): List<TransactionItem>
}

@Repository
interface UserPaymentTransactionRepository : BaseRepository<UserPaymentTransaction> {
    fun findAllByUserAndDeletedFalse(user: User, pageable: Pageable): Page<UserPaymentTransaction>

    @Query("select upt from UserPaymentTransaction upt where cast(upt.createdDate as date) = :date and upt.deleted = false")
    fun findAllByDateAndDeletedFalse(@Param("date") date: LocalDate, pageable: Pageable): Page<UserPaymentTransaction>

    @Query("select upt from UserPaymentTransaction upt where cast(upt.createdDate as date) between :startDate and :endDate and upt.deleted = false")
    fun findAllByDateRangeAndDeletedFalse(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        pageable: Pageable
    ): Page<UserPaymentTransaction>
}
