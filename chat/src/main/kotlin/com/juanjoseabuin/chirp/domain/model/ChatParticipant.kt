package com.juanjoseabuin.chirp.domain.model

import com.juanjoseabuin.chirp.domain.type.UserId

data class ChatParticipant(
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePictureUrl: String?
)
