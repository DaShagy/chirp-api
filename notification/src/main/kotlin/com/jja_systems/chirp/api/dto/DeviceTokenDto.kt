package com.jja_systems.chirp.api.dto

import com.jja_systems.chirp.domain.type.UserId
import java.time.Instant

data class DeviceTokenDto(
    val userId: UserId,
    val token: String,
    val createdAt: Instant
)
