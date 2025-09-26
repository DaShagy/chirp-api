package com.jja_systems.chirp.api.dto

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.ChatMessageId
import com.jja_systems.chirp.domain.type.UserId
import java.time.Instant

data class ChatMessageDto(
    val id: ChatMessageId,
    val chatId: ChatId,
    val content: String,
    val createdAt: Instant,
    val senderId: UserId
)
