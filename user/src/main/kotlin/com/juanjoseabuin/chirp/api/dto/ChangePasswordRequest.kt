package com.juanjoseabuin.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.juanjoseabuin.chirp.api.util.Password
import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:NotBlank
    @JsonProperty("oldPassword")
    val oldPassword: String,
    @field:Password
    @JsonProperty("newPassword")
    val newPassword: String
)
