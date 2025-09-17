package com.juanjoseabuin.chirp.infra.database.mapper

import com.juanjoseabuin.chirp.domain.model.EmailVerificationToken
import com.juanjoseabuin.chirp.infra.database.entity.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationTokenEntity(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}