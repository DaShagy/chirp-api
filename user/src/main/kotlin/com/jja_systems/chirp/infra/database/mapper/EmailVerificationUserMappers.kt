package com.jja_systems.chirp.infra.database.mapper

import com.jja_systems.chirp.domain.model.EmailVerificationToken
import com.jja_systems.chirp.infra.database.entity.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}