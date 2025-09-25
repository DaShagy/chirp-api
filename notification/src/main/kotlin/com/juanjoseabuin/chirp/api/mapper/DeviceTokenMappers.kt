package com.juanjoseabuin.chirp.api.mapper

import com.juanjoseabuin.chirp.api.dto.DeviceTokenDto
import com.juanjoseabuin.chirp.api.dto.PlatformDto
import com.juanjoseabuin.chirp.domain.model.DeviceToken

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