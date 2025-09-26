package com.jja_systems.chirp.domain.model

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.ChatMessageId
import java.time.Instant

data class ChatMessage(
    val id: ChatMessageId,
    val chatId: ChatId,
    val sender: ChatParticipant,
    val content: String,
    val createdAt: Instant
)
