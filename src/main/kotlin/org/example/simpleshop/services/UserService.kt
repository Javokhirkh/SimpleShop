package org.example.simpleshop.services

import jakarta.transaction.Transactional
import org.example.simpleshop.UserAlreadyExistsException
import org.example.simpleshop.UserMapper
import org.example.simpleshop.UserNotFoundException
import org.example.simpleshop.UserRepository
import org.example.simpleshop.dtoes.UserFullInformation
import org.example.simpleshop.dtoes.UserRequestDto
import org.example.simpleshop.dtoes.UserShortResponse
import org.example.simpleshop.dtoes.UserUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface UserService {
    fun createUser(user: UserRequestDto)
    fun getUserById(id: Long) : UserFullInformation
    fun getAllUsers(pageable: Pageable) : Page<UserShortResponse>
    fun updateUser(id: Long, user: UserUpdateRequest)
    fun deleteUser(id: Long)
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {
    @Transactional
    override fun createUser(user: UserRequestDto) {
        userRepository.findByUsername(user.username)?.let {
            throw UserAlreadyExistsException(user.username)
        } ?: run {
            val newUser = userMapper.toEntity(user)
            userRepository.save(newUser)
        }
    }

    override fun getUserById(id: Long): UserFullInformation {
        val user = userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()
        return userMapper.toFullInformation(user)
    }

    override fun getAllUsers(pageable: Pageable): Page<UserShortResponse> {
        val users = userRepository.findAllNotDeleted(pageable)
        return users.map { userMapper.toShortResponse(it) }
    }
    @Transactional
    override fun updateUser(id: Long, user: UserUpdateRequest) {
        val existingUser = userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()

        existingUser.apply {
            username = user.username ?: username
            fullName = user.fullName ?: fullName
            userRole = user.userRole ?: userRole
        }

        userRepository.save(existingUser)
    }

    @Transactional
    override fun deleteUser(id: Long) {
        val user = userRepository.findByIdAndDeletedFalse(id)
            ?: throw UserNotFoundException()
        userRepository.trash(id)
    }


}