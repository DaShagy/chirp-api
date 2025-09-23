package com.juanjoseabuin.chirp.domain.event

import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.domain.type.UserId

data class ChatParticipantLeftEvent(
    val chatId: ChatId,
    val userId: UserId
)
