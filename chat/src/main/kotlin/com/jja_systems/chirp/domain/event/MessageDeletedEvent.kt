package com.jja_systems.chirp.domain.event

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.ChatMessageId

data class MessageDeletedEvent (
    val messageId: ChatMessageId,
    val chatId: ChatId
)