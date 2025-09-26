package com.jja_systems.chirp.domain.event

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.UserId

data class ChatCreatedEvent(
    val chatId: ChatId,
    val participantIds: List<UserId>
)
