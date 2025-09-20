package com.juanjoseabuin.chirp.api.util

import com.juanjoseabuin.chirp.domain.exception.UnauthorizedException
import com.juanjoseabuin.chirp.domain.type.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()