package org.example.simpleshop

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.Transient
import org.hibernate.annotations.ColumnDefault
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: LocalDateTime? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: LocalDateTime? = null,
    @CreatedBy var createdBy: String? = null,
    @LastModifiedBy var lastModifiedBy: String? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity
@Table(name = "categories")
class Category(
    var name: String,
    @Column(name = "\"order\"") var order: Long,
    var description: String,
) : BaseEntity()

@Entity
@Table(name = "pruducts")
class Product(
    var name: String,
    var count: Long,
    @ManyToOne var category: Category
) : BaseEntity()

@Entity
@Table(name = "transaction_items")
class TransactionItem(
    @ManyToOne var product: Product,
    var count: Long,
    var amount: BigDecimal,
    var totalAmount: BigDecimal,
    @ManyToOne var transaction: Transaction
) : BaseEntity()

@Entity
@Table(name = "transactions")
class Transaction(
    @ManyToOne var user :User,
    var totalAmount: BigDecimal,
    var date: LocalDate,
) : BaseEntity()

@Entity
@Table(name = "users")
class User(
    @Column(unique = true) var username: String,
    var fullName: String,
    var balance: BigDecimal,
    var userRole: UserRole
) : BaseEntity()

@Entity
@Table(name = "user_payment_transactions")
class UserPaymentTransaction(
    @ManyToOne var user: User,
    var amount: BigDecimal,
    var date: LocalDate,
) : BaseEntity()


@Embeddable
class LocalizedName(
    @Column(length = 50) var uz: String,
    @Column(length = 50) var ru: String,
    @Column(length = 50) var en: String,
) {
    @Transient
    fun localized(): String {
        return when (LocaleContextHolder.getLocale().language) {
            "en" -> this.en
            "ru" -> this.ru
            else -> this.uz
        }
    }
}