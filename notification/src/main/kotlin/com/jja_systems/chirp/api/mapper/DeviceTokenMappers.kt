package com.jja_systems.chirp.api.mapper

import com.jja_systems.chirp.api.dto.DeviceTokenDto
import com.jja_systems.chirp.api.dto.PlatformDto
import com.jja_systems.chirp.domain.model.DeviceToken

fun DeviceToken.toDeviceTokenDto(): DeviceTokenDto {
    return DeviceTokenDto(
        userId = userId,
        token = token,
        createdAt = createdAt
    )
}

fun PlatformDto.toDeviceTokenPlatform(): DeviceToken.Platform {
    return when (this) {
        PlatformDto.ANDROID -> DeviceToken.Platform.ANDROID
        PlatformDto.IOS -> DeviceToken.Platform.IOS
    }
}