package com.jja_systems.chirp.domain.event

import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.UserId

data class ChatParticipantsJoinedEvent(
    val chatId: ChatId,
    val userIds: Set<UserId>
)
