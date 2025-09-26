package com.jja_systems.chirp.infra.database.mapper

import com.jja_systems.chirp.domain.model.User
import com.jja_systems.chirp.infra.database.entity.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasVerifiedEmail = hasVerifiedEmail
    )
}