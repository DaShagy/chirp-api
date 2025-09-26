package com.jja_systems.chirp.api.dto

import com.jja_systems.chirp.domain.type.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean
)
