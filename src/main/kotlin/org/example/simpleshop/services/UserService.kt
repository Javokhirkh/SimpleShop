package org.example.simpleshop.services

import org.example.simpleshop.User
import org.example.simpleshop.UserAlreadyExistsException
import org.example.simpleshop.UserNotFoundException
import org.example.simpleshop.UserRepository
import org.example.simpleshop.dtoes.UserRequestDto
import org.example.simpleshop.dtoes.UserResponseDto
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service


interface UserService {
    fun createUser(user: UserRequestDto) : UserResponseDto
    fun getUserById(id: Long) : UserResponseDto
    fun getAllUsers() : Page<UserResponseDto>
    fun updateUser(id: Long, user: UserRequestDto) : UserResponseDto
    fun deleteUser(id: Long)
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun createUser(user: UserRequestDto): UserResponseDto {
        if(userRepository.existsByUsername(user.username)) {
            throw UserAlreadyExistsException("Username already exists")
        }
        return userRepository.save(user.toEntity()).toUserResponse()
    }

    override fun getUserById(id: Long): UserResponseDto {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }
        return user.toUserResponse()
    }

    override fun getAllUsers(): Page<UserResponseDto> {
        return userRepository.findAll().map { it.toUserResponse() } as Page<UserResponseDto>
    }

    override fun updateUser(
        id: Long,
        user: UserRequestDto
    ): UserResponseDto {
        val existingUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }

        val updatedUser = User(
            username = user.username,
            fullName = user.fullName,
            balance = user.balance,
            userRole = user.userRole,
        )


        return userRepository.save(updatedUser).toUserResponse()
    }

    override fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }
        userRepository.trash(id)
    }
    private fun UserRequestDto.toEntity(): User {
        return User(
            username = this.username,
            fullName = this.fullName,
            balance = this.balance,
            userRole = this.userRole
        )

    }
    private fun User.toUserResponse(): UserResponseDto {
        return UserResponseDto(
            id = this.id,
            username = this.username,
            fullName = this.fullName,
            balance = this.balance,
            userRole = this.userRole,
            createdDate = this.createdDate,
            modifiedDate = this.modifiedDate,
            createdBy = this.createdBy,
            lastModifiedBy = this.lastModifiedBy
        )
    }
}