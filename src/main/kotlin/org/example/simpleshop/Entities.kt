package org.example.simpleshop

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
import java.time.LocalDateTime


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
    @field:Column(nullable = false,unique = true)var name: String,
    @field:Column(name = "\"order\"" ) var order: Long?=100,
    var description: String?,
) : BaseEntity()

@Entity
@Table(name = "pruducts")
class Product(
    @Column(nullable = false)var name: String,
    var count: Long?=0,
    @ManyToOne var category: Category
) : BaseEntity()

@Entity
@Table(name = "transaction_items")
class TransactionItem(
    var count: Long,
    var amount: BigDecimal,
    var totalAmount: BigDecimal,
    @ManyToOne var product: Product,
    @ManyToOne var transaction: Transaction
) : BaseEntity()

@Entity
@Table(name = "transactions")
class Transaction(
    var totalAmount: BigDecimal,
    @ManyToOne var user :User,
) : BaseEntity()

@Entity
@Table(name = "users")
class User(
    @field:Column(unique = true, nullable = false) var username: String,
    var fullName: String?,
    var balance: BigDecimal= BigDecimal.ZERO,
    @field:Enumerated(EnumType.STRING)
    @field:Column(nullable = false, length = 32)var userRole: UserRole
) : BaseEntity()

@Entity
@Table(name = "user_payment_transactions")
class UserPaymentTransaction(
    var amount: BigDecimal,
    @ManyToOne var user: User,
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