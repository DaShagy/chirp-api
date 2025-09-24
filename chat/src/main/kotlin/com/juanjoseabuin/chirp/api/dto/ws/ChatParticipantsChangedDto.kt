package com.juanjoseabuin.chirp.api.dto.ws

import com.juanjoseabuin.chirp.domain.type.ChatId

data class ChatParticipantsChangedDto(
    val chatId: ChatId
)
