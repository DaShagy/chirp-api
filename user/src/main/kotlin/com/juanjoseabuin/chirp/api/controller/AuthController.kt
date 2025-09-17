package com.juanjoseabuin.chirp.api.controller

import com.juanjoseabuin.chirp.api.dto.AuthenticatedUserDto
import com.juanjoseabuin.chirp.api.dto.LoginRequest
import com.juanjoseabuin.chirp.api.dto.RegisterRequest
import com.juanjoseabuin.chirp.api.dto.UserDto
import com.juanjoseabuin.chirp.api.mapper.toDto
import com.juanjoseabuin.chirp.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
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
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toDto()
    }
}