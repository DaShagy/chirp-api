package com.jja_systems.chirp.api.dto.ws

import com.jja_systems.chirp.domain.type.UserId

data class ProfilePictureUpdateDto(
    val userId: UserId,
    val newUrl: String?
)
