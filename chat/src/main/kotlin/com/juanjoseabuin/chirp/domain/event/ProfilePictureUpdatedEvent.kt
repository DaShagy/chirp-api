package com.juanjoseabuin.chirp.domain.event

import com.juanjoseabuin.chirp.domain.type.UserId

data class ProfilePictureUpdatedEvent(
    val userId: UserId,
    val newUrl: String?
)