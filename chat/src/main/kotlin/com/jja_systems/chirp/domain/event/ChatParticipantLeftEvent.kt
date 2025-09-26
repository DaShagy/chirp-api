package com.jja_systems.chirp.domain.event

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.UserId

data class ChatParticipantLeftEvent(
    val chatId: ChatId,
    val userId: UserId
)
