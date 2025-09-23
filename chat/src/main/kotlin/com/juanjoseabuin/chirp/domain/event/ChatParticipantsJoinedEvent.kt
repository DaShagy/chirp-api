package com.juanjoseabuin.chirp.domain.event

import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.domain.type.UserId

data class ChatParticipantsJoinedEvent(
    val chatId: ChatId,
    val userIds: Set<UserId>
)
