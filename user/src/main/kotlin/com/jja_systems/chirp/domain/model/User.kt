package com.jja_systems.chirp.domain.model

import com.jja_systems.chirp.domain.type.UserId

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasVerifiedEmail: Boolean
)
