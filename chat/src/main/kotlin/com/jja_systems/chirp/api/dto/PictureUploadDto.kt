package com.jja_systems.chirp.api.dto

import java.time.Instant

data class PictureUploadDto(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>,
    val expiresAt: Instant
)