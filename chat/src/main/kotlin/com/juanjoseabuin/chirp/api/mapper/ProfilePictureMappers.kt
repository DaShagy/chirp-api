package com.juanjoseabuin.chirp.api.mapper

import com.juanjoseabuin.chirp.api.dto.PictureUploadDto
import com.juanjoseabuin.chirp.domain.model.ProfilePictureUploadCredentials

fun ProfilePictureUploadCredentials.toDto(): PictureUploadDto {
    return PictureUploadDto(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers,
        expiresAt = expiresAt
    )
}