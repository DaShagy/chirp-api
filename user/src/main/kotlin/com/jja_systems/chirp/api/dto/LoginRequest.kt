package com.jja_systems.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequest(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("password")
    val password: String
)
