package com.juanjoseabuin.chirp.domain.model

import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.domain.type.ChatMessageId
import java.time.Instant

data class ChatMessage(
    val id: ChatMessageId,
    val chatId: ChatId,
    val sender: ChatParticipant,
    val content: String,
    val createdAt: Instant
)
