package com.juanjoseabuin.chirp.service

import com.juanjoseabuin.chirp.domain.exception.InvalidCredentialsException
import com.juanjoseabuin.chirp.domain.exception.InvalidTokenException
import com.juanjoseabuin.chirp.domain.exception.SamePasswordException
import com.juanjoseabuin.chirp.domain.exception.UserNotFoundException
import com.juanjoseabuin.chirp.domain.type.UserId
import com.juanjoseabuin.chirp.infra.database.entity.PasswordResetTokenEntity
import com.juanjoseabuin.chirp.infra.database.repository.PasswordResetTokenRepository
import com.juanjoseabuin.chirp.infra.database.repository.RefreshTokenRepository
import com.juanjoseabuin.chirp.infra.database.repository.UserRepository
import com.juanjoseabuin.chirp.infra.security.PasswordEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    @param:Value("\${chirp.email.reset-password.expiry-minutes}") private val expiryMinutes: Long
) {
    @Transactional
    fun requestPasswordReset(email: String) {
        val userEntity = userRepository.findByEmail(email) ?: return

        passwordResetTokenRepository.invalidateActiveTokensForUser(userEntity)

        val token = PasswordResetTokenEntity(
            user = userEntity,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES),
        )
        passwordResetTokenRepository.save(token)

        //TODO: Inform notification service about password reset trigger to send email
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String) {
        val resetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if (resetToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used")
        }

        if (resetToken.isExpired) {
            throw InvalidTokenException("Email verification token has already expired")
        }

        val userEntity = resetToken.user

        if(passwordEncoder.matches(newPassword, userEntity.hashedPassword)) {
            throw SamePasswordException()
        }
        val hashedNewPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            userEntity.apply {
                this.hashedPassword = hashedNewPassword
            }
        )

        passwordResetTokenRepository.save(
            resetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        refreshTokenRepository.deleteByUserId(userEntity.id!!)
    }

    @Transactional
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String
    ) {
        val userEntity = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(oldPassword, userEntity.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if(oldPassword == newPassword) {
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(userEntity.id!!)

        val newHashedPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            userEntity.apply {
                this.hashedPassword = newHashedPassword
            }
        )
    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteByExpiresAtLessThan(
            now = Instant.now()
        )
    }
}