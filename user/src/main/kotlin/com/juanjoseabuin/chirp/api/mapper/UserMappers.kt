package com.juanjoseabuin.chirp.api.mapper

import com.juanjoseabuin.chirp.api.dto.AuthenticatedUserDto
import com.juanjoseabuin.chirp.api.dto.UserDto
import com.juanjoseabuin.chirp.domain.model.AuthenticatedUser
import com.juanjoseabuin.chirp.domain.model.User

fun AuthenticatedUser.toDto(): AuthenticatedUserDto {
    return AuthenticatedUserDto(
        user = user.toDto(),
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
    )
}