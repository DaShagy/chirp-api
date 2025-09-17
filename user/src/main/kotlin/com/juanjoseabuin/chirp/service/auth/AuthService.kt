package com.juanjoseabuin.chirp.service.auth

import com.juanjoseabuin.chirp.domain.exception.InvalidCredentialsException
import com.juanjoseabuin.chirp.domain.exception.UserAlreadyExistsException
import com.juanjoseabuin.chirp.domain.exception.UserNotFoundException
import com.juanjoseabuin.chirp.domain.model.AuthenticatedUser
import com.juanjoseabuin.chirp.domain.model.User
import com.juanjoseabuin.chirp.domain.model.UserId
import com.juanjoseabuin.chirp.infra.database.entity.RefreshTokenEntity
import com.juanjoseabuin.chirp.infra.database.entity.UserEntity
import com.juanjoseabuin.chirp.infra.database.mapper.toUser
import com.juanjoseabuin.chirp.infra.database.repository.RefreshTokenRepository
import com.juanjoseabuin.chirp.infra.database.repository.UserRepository
import com.juanjoseabuin.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
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

    fun login(email: String, password: String): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim()) ?: throw InvalidCredentialsException()

        if(!passwordEncoder.matches(password, user.hashedPasswords)) {
            throw InvalidCredentialsException()
        }

        // TODO: Check for verified email

        return user.id?.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, refreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashedToken = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedToken
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}