package com.jja_systems.chirp.service

import com.jja_systems.chirp.domain.event.user.UserEvent
import com.jja_systems.chirp.domain.exception.EmailNotVerifiedException
import com.jja_systems.chirp.domain.exception.InvalidCredentialsException
import com.jja_systems.chirp.domain.exception.InvalidTokenException
import com.jja_systems.chirp.domain.exception.UserAlreadyExistsException
import com.jja_systems.chirp.domain.exception.UserNotFoundException
import com.jja_systems.chirp.domain.model.AuthenticatedUser
import com.jja_systems.chirp.domain.model.User
import com.jja_systems.chirp.domain.type.UserId
import com.jja_systems.chirp.infra.database.entity.RefreshTokenEntity
import com.jja_systems.chirp.infra.database.entity.UserEntity
import com.jja_systems.chirp.infra.database.mapper.toUser
import com.jja_systems.chirp.infra.database.repository.RefreshTokenRepository
import com.jja_systems.chirp.infra.database.repository.UserRepository
import com.jja_systems.chirp.infra.message_queue.EventPublisher
import com.jja_systems.chirp.infra.security.PasswordEncoder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val emailVerificationService: EmailVerificationService,
    private val eventPublisher: EventPublisher
) {
    fun register(email: String, username: String, password: String): User {
        val trimmedEmail = email.trim()
        val trimmedUsername = username.trim()
        val users = userRepository.findByEmailOrUsername(
            email = trimmedEmail,
            username = trimmedUsername
        )

        if (users.isNotEmpty()) {
            throw UserAlreadyExistsException()
        }


        val savedUser = userRepository.save(
            UserEntity(
                email = trimmedEmail,
                username = trimmedUsername,
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()

        val token = emailVerificationService.createVerificationToken(trimmedEmail)

        eventPublisher.publish(
            event = UserEvent.Created(
                userId = savedUser.id,
                email = savedUser.email,
                username = savedUser.username,
                verificationToken = token.token
            )
        )

        return savedUser
    }

    fun login(email: String, password: String): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim()) ?: throw InvalidCredentialsException()

        if(!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if(!user.hasVerifiedEmail) {
            throw EmailNotVerifiedException()
        }

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

    @Transactional
    fun refresh(refreshToken: String): AuthenticatedUser {
        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException(
                message = "Invalid refresh token"
            )
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        val hashedRefreshToken = hashToken(refreshToken)

        return user.id?.let { userId ->
            refreshTokenRepository.findByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashedRefreshToken
            ) ?: throw InvalidTokenException("Invalid refresh token")

            refreshTokenRepository.deleteByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashedRefreshToken
            )

            val newAccessToken = jwtService.generateAccessToken(userId)
            val newRefreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, newRefreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional
    fun logout(refreshToken: String) {
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val hashedToken = hashToken(refreshToken)
        refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashedToken)
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