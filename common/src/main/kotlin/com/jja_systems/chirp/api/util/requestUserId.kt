package com.jja_systems.chirp.api.util

import com.jja_systems.chirp.domain.exception.UnauthorizedException
import com.jja_systems.chirp.domain.type.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()