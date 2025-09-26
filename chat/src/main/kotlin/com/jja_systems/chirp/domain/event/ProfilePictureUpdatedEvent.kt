package com.jja_systems.chirp.domain.event

import com.jja_systems.chirp.domain.type.UserId

data class ProfilePictureUpdatedEvent(
    val userId: UserId,
    val newUrl: String?
)