package com.juanjoseabuin.chirp.api.controller

import com.juanjoseabuin.chirp.api.dto.DeviceTokenDto
import com.juanjoseabuin.chirp.api.dto.RegisterDeviceRequest
import com.juanjoseabuin.chirp.api.mapper.toDeviceTokenDto
import com.juanjoseabuin.chirp.api.mapper.toDeviceTokenPlatform
import com.juanjoseabuin.chirp.api.util.requestUserId
import com.juanjoseabuin.chirp.service.PushNotificationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notification")
class DeviceTokenController(
    private val pushNotificationService: PushNotificationService
) {

    @PostMapping("/register")
    fun registerDeviceToken(
        @Valid @RequestBody body: RegisterDeviceRequest
    ): DeviceTokenDto {
        return pushNotificationService.registerDevice(
            userId = requestUserId,
            token = body.token,
            platform = body.platform.toDeviceTokenPlatform()
        ).toDeviceTokenDto()
    }

    @DeleteMapping("/{token}")
    fun unregisterDeviceToken(
        @PathVariable("token") token: String
    ) {
        pushNotificationService.unregisterDevice(token)
    }
}