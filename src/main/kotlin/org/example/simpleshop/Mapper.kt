package org.example.simpleshop

import org.example.simpleshop.dtoes.CategoryFullResponse
import org.example.simpleshop.dtoes.CategoryRequestDto
import org.example.simpleshop.dtoes.ProductFullResponse
import org.example.simpleshop.dtoes.ProductRequest
import org.example.simpleshop.dtoes.TransactionFullResponse
import org.example.simpleshop.dtoes.TransactionItemRequest
import org.example.simpleshop.dtoes.TransactionItemResponse
import org.example.simpleshop.dtoes.TransactionShortResponse
import org.example.simpleshop.dtoes.UserFullInformation
import org.example.simpleshop.dtoes.UserPaymentTransactionResponseFullInformation
import org.example.simpleshop.dtoes.UserPaymentTransactionShortResponse
import org.example.simpleshop.dtoes.UserRequestDto
import org.example.simpleshop.dtoes.UserShortResponse
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class UserMapper {

    fun toEntity(user: UserRequestDto):User{
        user.run {
            return User(
                username = username,
                fullName = fullName,
                balance = balance,
                userRole = userRole
            )
        }
    }

    fun toShortResponse(user: User): UserShortResponse {
        user.run {
            return UserShortResponse(
                id = id!!,
                username = username,
                fullName = fullName,
                balance = balance,
                userRole = userRole
            )
        }
    }
    fun toFullInformation(user: User): UserFullInformation {
        user.run {
            return org.example.simpleshop.dtoes.UserFullInformation(
                id = id!!,
                username = username,
                fullName = fullName,
                balance = balance,
                userRole = userRole,
                createdDate = createdDate,
                modifiedDate = modifiedDate,
                createdBy = createdBy,
                lastModifiedBy = lastModifiedBy
            )
        }
    }
}

@Component
class UserPaymentTransactionMapper(
    private val userMapper: UserMapper
) {
    fun toShortResponse(transaction: UserPaymentTransaction): org.example.simpleshop.dtoes.UserPaymentTransactionShortResponse {
        transaction.run {
            return UserPaymentTransactionShortResponse(
                id = id!!,
                amount = amount,
                user = userMapper.toShortResponse(user)
            )
        }
    }

    fun toFullInformation(transaction: UserPaymentTransaction): org.example.simpleshop.dtoes.UserPaymentTransactionResponseFullInformation {
        transaction.run {
            return UserPaymentTransactionResponseFullInformation(
                id = id!!,
                user = userMapper.toShortResponse(user),
                amount = amount,
                createdDate = createdDate,
                modifiedDate = modifiedDate,
                createdBy = createdBy,
                lastModifiedBy = lastModifiedBy
            )
        }
    }
    fun toEntity(user: User, amount: BigDecimal): UserPaymentTransaction {
        return UserPaymentTransaction(
            user = user,
            amount = amount
        )
    }
}
@Component
class TransactionMapper {
    fun toTransactionFullResponseDto(entity: Transaction,items:List<TransactionItemResponse>): TransactionFullResponse {
        entity.run {
            return TransactionFullResponse(
                id = id!!,
                user = UserMapper().toShortResponse(user),
                totalAmount = totalAmount,
                createdDate = createdDate,
                items=items
            )
        }
    }

    fun toTransactionShortResponseDto(it:Transaction): TransactionShortResponse {
        return TransactionShortResponse(
            id = it.id!!,
            user = UserMapper().toShortResponse(it.user),
            totalAmount = it.totalAmount,
            createdDate = it.createdDate
        )
    }



}@Component
class TransactionItemMapper {
    fun toEntity(dto: TransactionItemRequest,product: Product,transaction: Transaction,totalAmount: BigDecimal): TransactionItem {
        return TransactionItem(
            count=dto.count,
            product=product,
            transaction=transaction,
            amount = dto.amount,
            totalAmount = totalAmount

        )

    }
    fun toResponse(entity: TransactionItem): TransactionItemResponse {
        entity.run {
            return TransactionItemResponse(
                id = id!!,
                quantity = count,
                pricePerUnit = amount,
                totalPrice = totalAmount,
                transaction = transaction,
                product = product,
                createdAt = createdDate
            )
        }
    }

}

@Component
class CategoryMapper {
    fun toEntity(dto: CategoryRequestDto): Category {
        return Category(
            name = dto.name,
            description = dto.description
        )
    }
    fun toFullResponse(entity: Category): CategoryFullResponse {
        entity.run {
            return CategoryFullResponse(
                id = id!!,
                name = name,
                description = description,
                createdDate = createdDate,
                modifiedDate = modifiedDate,
                createdBy = createdBy,
                lastModifiedBy = lastModifiedBy,
                order = order

            )
        }
    }

    fun toShortResponse(entity: Category): org.example.simpleshop.dtoes.CategoryShortResponse {
        entity.run {
            return org.example.simpleshop.dtoes.CategoryShortResponse(
                id = id!!,
                name = name,
                description = description,
                order = order
            )
        }
    }
}
@Component
class ProductMapper(){
    fun toShortResponse(entity: Product,categoryFullResponse: CategoryFullResponse): org.example.simpleshop.dtoes.ProductShortResponse {
        entity.run {
            return org.example.simpleshop.dtoes.ProductShortResponse(
                id = id!!,
                name = name,
                count = count?:0,
                category = categoryFullResponse
            )
        }

    }
    fun toFullResponse(entity: Product,categoryFullResponse: CategoryFullResponse): ProductFullResponse {
        entity.run {
            return ProductFullResponse(
                id = id!!,
                name = name,
                count = count?:0,
                category = categoryFullResponse,
                createdDate = createdDate,
                modifiedDate = modifiedDate,
                createdBy = createdBy,
                lastModifiedBy = lastModifiedBy
            )
        }
    }

    fun toEntity(dto: ProductRequest,category: Category): Product {
        dto.run {
            return Product(
                name = name,
                count = count,
                category = category
            )
        }
    }


}