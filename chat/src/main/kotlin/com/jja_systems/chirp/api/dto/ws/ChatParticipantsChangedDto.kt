package com.jja_systems.chirp.api.dto.ws

import com.jja_systems.chirp.domain.type.ChatId

data class ChatParticipantsChangedDto(
    val chatId: ChatId
)
