package org.example.simpleshop.dtoes

import org.example.simpleshop.UserRole
import java.math.BigDecimal
import java.time.LocalDateTime

data class UserRequestDto(
    val username: String,
    val fullName: String,
    val balance: BigDecimal,
    val userRole: UserRole,
)

data class UserResponseDto(
    val id: Long?,
    val username: String,
    val fullName: String,
    val balance: BigDecimal,
    val userRole: UserRole,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)
