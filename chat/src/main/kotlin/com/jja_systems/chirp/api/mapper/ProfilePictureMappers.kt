package com.jja_systems.chirp.api.mapper

import com.jja_systems.chirp.api.dto.PictureUploadDto
import com.jja_systems.chirp.domain.model.ProfilePictureUploadCredentials

fun ProfilePictureUploadCredentials.toDto(): PictureUploadDto {
    return PictureUploadDto(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers,
        expiresAt = expiresAt
    )
}