package org.example.simpleshop.dtoes

import jakarta.validation.constraints.NotBlank
import org.example.simpleshop.UserRole
import java.math.BigDecimal
import java.time.LocalDateTime

data class UserRequestDto(
    @get:NotBlank(message = "USER_NOT_NULL")val username: String,
    val fullName: String?,
    val balance: BigDecimal,
    val userRole: UserRole,
)

data class UserFullInformation(
    val id: Long,
    val username: String,
    val fullName: String?,
    val balance: BigDecimal,
    val userRole: UserRole,
    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: String?,
    val lastModifiedBy: String?,
)

data class UserUpdateRequest(
    val username: String?,
    val fullName: String?,
    val userRole: UserRole?,
)

data class UserShortResponse(
    val id: Long,
    val username: String,
    val fullName: String?,
    val balance: BigDecimal,
    val userRole: UserRole,
)
