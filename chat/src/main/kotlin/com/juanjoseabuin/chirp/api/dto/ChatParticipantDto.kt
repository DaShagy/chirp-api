package com.juanjoseabuin.chirp.api.dto

import com.juanjoseabuin.chirp.domain.type.UserId

data class ChatParticipantDto(
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePictureUrl: String?
)