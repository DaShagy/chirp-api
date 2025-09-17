package com.juanjoseabuin.chirp.service.auth

import com.juanjoseabuin.chirp.domain.exception.UserAlreadyExistsException
import com.juanjoseabuin.chirp.domain.model.User
import com.juanjoseabuin.chirp.infra.database.entity.UserEntity
import com.juanjoseabuin.chirp.infra.database.mapper.toUser
import com.juanjoseabuin.chirp.infra.database.repository.UserRepository
import com.juanjoseabuin.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(email: String, username: String, password: String): User {
        val user = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim()
        )

        if (user != null) {
            throw UserAlreadyExistsException()
        }

        val savedUser = userRepository.save(
            UserEntity(
                email = email.trim(),
                username = username.trim(),
                hashedPasswords = passwordEncoder.encode(password)
            )
        ).toUser()

        return savedUser
    }
}