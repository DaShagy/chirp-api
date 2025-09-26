package com.jja_systems.chirp.api.controller

import com.jja_systems.chirp.api.config.IpRateLimit
import com.jja_systems.chirp.api.dto.AuthenticatedUserDto
import com.jja_systems.chirp.api.dto.ChangePasswordRequest
import com.jja_systems.chirp.api.dto.EmailRequest
import com.jja_systems.chirp.api.dto.LoginRequest
import com.jja_systems.chirp.api.dto.RefreshRequest
import com.jja_systems.chirp.api.dto.RegisterRequest
import com.jja_systems.chirp.api.dto.ResetPasswordRequest
import com.jja_systems.chirp.api.dto.UserDto
import com.jja_systems.chirp.api.mapper.toDto
import com.jja_systems.chirp.api.util.requestUserId
import com.jja_systems.chirp.infra.rate_limiting.EmailRateLimiter
import com.jja_systems.chirp.service.AuthService
import com.jja_systems.chirp.service.EmailVerificationService
import com.jja_systems.chirp.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService,
    private val emailRateLimiter: EmailRateLimiter
) {

    @PostMapping("/register")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        ).toDto()
    }

    @PostMapping("/login")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toDto()
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService.refresh(
            refreshToken = body.refreshToken
        ).toDto()
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody body: RefreshRequest
    ) {
        authService.logout(
            refreshToken = body.refreshToken
        )
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @Valid @RequestBody body: ResetPasswordRequest
    ) {
        passwordResetService.resetPassword(
            token = body.token,
            newPassword = body.newPassword
        )
    }

    @PostMapping("/resend-verification")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun resendVerification(
        @Valid @RequestBody body: EmailRequest
    ) {
        emailRateLimiter.withRateLimit(body.email) {
            emailVerificationService.resendVerificationEmail(body.email)
        }
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
        passwordResetService.changePassword(
            userId = requestUserId,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword
        )
    }

    @PostMapping("/forgot-password")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ) {
        passwordResetService.requestPasswordReset(
            email = body.email
        )
    }
}