package com.juanjoseabuin.chirp.api.dto.ws

import com.juanjoseabuin.chirp.domain.type.UserId

data class ProfilePictureUpdateDto(
    val userId: UserId,
    val newUrl: String?
)
