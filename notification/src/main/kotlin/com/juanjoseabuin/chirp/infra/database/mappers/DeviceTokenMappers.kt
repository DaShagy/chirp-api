package com.juanjoseabuin.chirp.infra.database.mappers

import com.juanjoseabuin.chirp.domain.model.DeviceToken
import com.juanjoseabuin.chirp.infra.database.DeviceTokenEntity

fun DeviceTokenEntity.toDeviceToken(): DeviceToken {
    return DeviceToken(
        id = id,
        userId = userId,
        token = token,
        platform = platform.toDeviceTokenPlatform(),
        createdAt = createdAt
    )
}