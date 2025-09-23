package com.juanjoseabuin.chirp.domain.event

import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.domain.type.ChatMessageId

data class MessageDeletedEvent (
    val messageId: ChatMessageId,
    val chatId: ChatId
)