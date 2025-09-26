package com.jja_systems.chirp.api.mapper

import com.jja_systems.chirp.api.dto.AuthenticatedUserDto
import com.jja_systems.chirp.api.dto.UserDto
import com.jja_systems.chirp.domain.model.AuthenticatedUser
import com.jja_systems.chirp.domain.model.User

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