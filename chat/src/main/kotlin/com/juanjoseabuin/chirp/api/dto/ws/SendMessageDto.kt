package com.juanjoseabuin.chirp.api.dto.ws

import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.domain.type.ChatMessageId

data class SendMessageDto(
    val chatId: ChatId,
    val content: String,
    val messageId: ChatMessageId? = null
)
