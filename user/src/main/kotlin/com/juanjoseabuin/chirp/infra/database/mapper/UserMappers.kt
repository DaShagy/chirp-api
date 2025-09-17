package com.juanjoseabuin.chirp.infra.database.mapper

import com.juanjoseabuin.chirp.domain.model.User
import com.juanjoseabuin.chirp.infra.database.entity.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasVerifiedEmail = hasVerifiedEmail
    )
}