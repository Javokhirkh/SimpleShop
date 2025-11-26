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
import org.springframework.data.repository.findByIdOrNull
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
interface CategoryRepository : BaseRepository<Category>, CustomCategoryRepository {
    override fun findByNameAndDeletedFalse(name: String): Category?

    override fun findAllByDeletedFalseOrderByOrder(): List<Category>
}

@Repository
interface ProductRepository : BaseRepository<Product> {
    fun findByNameAndDeletedFalse(name: String): List<Product>

    fun findByCategoryIdAndDeletedFalse(categoryId: Long): List<Product>

    fun findByCategoryIdAndDeletedFalse(categoryId: Long, pageable: Pageable): Page<Product>

    fun findByCountGreaterThanAndDeletedFalse(count: Long): List<Product>
}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByUsernameAndDeletedFalse(username: String): User?

    fun findByFullNameAndDeletedFalse(fullName: String): User?
    fun existsByUsername(username: String): Boolean
}

@Repository
interface TransactionRepository : BaseRepository<Transaction> {
    fun findByUserIdAndDeletedFalse(userId: Long): List<Transaction>

    fun findByUserIdAndDeletedFalse(userId: Long, pageable: Pageable): Page<Transaction>

    fun findByDateAndDeletedFalse(date: LocalDate): List<Transaction>

    fun findByDateBetweenAndDeletedFalse(startDate: LocalDate, endDate: LocalDate): List<Transaction>

    fun findByDateBetweenAndDeletedFalse(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<Transaction>
}

@Repository
interface TransactionItemRepository : BaseRepository<TransactionItem> {
    fun findByTransactionIdAndDeletedFalse(transactionId: Long): List<TransactionItem>

    fun findByProductIdAndDeletedFalse(productId: Long): List<TransactionItem>

    fun findByProductIdAndDeletedFalse(productId: Long, pageable: Pageable): Page<TransactionItem>
}

@Repository
interface UserPaymentTransactionRepository : BaseRepository<UserPaymentTransaction> {
    fun findByUserIdAndDeletedFalse(userId: Long): List<UserPaymentTransaction>

    fun findByUserIdAndDeletedFalse(userId: Long, pageable: Pageable): Page<UserPaymentTransaction>

    fun findByDateAndDeletedFalse(date: LocalDate): List<UserPaymentTransaction>

    fun findByDateBetweenAndDeletedFalse(startDate: LocalDate, endDate: LocalDate): List<UserPaymentTransaction>

    fun findByDateBetweenAndDeletedFalse(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<UserPaymentTransaction>
}

@NoRepositoryBean
interface CustomCategoryRepository {
    fun findByNameAndDeletedFalse(name: String): Category?

    fun findAllByDeletedFalseOrderByOrder(): List<Category>
}

class CustomCategoryRepositoryImpl(
    private val entityManager: EntityManager,
) : CustomCategoryRepository {

    override fun findByNameAndDeletedFalse(name: String): Category? {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Category::class.java)
        val root = query.from(Category::class.java)

        query.where(
            cb.and(
                cb.equal(root.get<String>("name"), name),
                cb.equal(root.get<Boolean>("deleted"), false)
            )
        )

        return entityManager.createQuery(query).resultList.firstOrNull()
    }

    override fun findAllByDeletedFalseOrderByOrder(): List<Category> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Category::class.java)
        val root = query.from(Category::class.java)

        query.where(cb.equal(root.get<Boolean>("deleted"), false))
        query.orderBy(cb.asc(root.get<Long>("order")))

        return entityManager.createQuery(query).resultList
    }
}