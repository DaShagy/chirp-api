package com.juanjoseabuin.chirp.api.dto

import com.juanjoseabuin.chirp.domain.type.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean
)
