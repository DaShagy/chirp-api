package com.jja_systems.chirp.infra.database.mappers

import com.jja_systems.chirp.domain.model.DeviceToken
import com.jja_systems.chirp.infra.database.DeviceTokenEntity

fun DeviceTokenEntity.toDeviceToken(): DeviceToken {
    return DeviceToken(
        id = id,
        userId = userId,
        token = token,
        platform = platform.toDeviceTokenPlatform(),
        createdAt = createdAt
    )
}