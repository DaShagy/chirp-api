package com.jja_systems.chirp.domain.exception

import com.jja_systems.chirp.domain.type.UserId

class ChatParticipantNotFoundException(
    private val id: UserId
) : RuntimeException(
    "The chat participant with id $id was not found."
)