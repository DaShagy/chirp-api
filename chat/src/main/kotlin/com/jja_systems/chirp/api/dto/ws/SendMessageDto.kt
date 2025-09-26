package com.jja_systems.chirp.api.dto.ws

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.ChatMessageId

data class SendMessageDto(
    val chatId: ChatId,
    val content: String,
    val messageId: ChatMessageId? = null
)
