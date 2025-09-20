package com.juanjoseabuin.chirp.domain.model

import com.juanjoseabuin.chirp.domain.type.UserId

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasVerifiedEmail: Boolean
)
