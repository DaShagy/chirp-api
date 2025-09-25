package com.juanjoseabuin.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class RegisterDeviceRequest(
    @field:NotBlank
    @JsonProperty("token")
    val token: String,
    @JsonProperty("platform")
    val platform: PlatformDto
)

enum class PlatformDto {
    ANDROID, IOS
}