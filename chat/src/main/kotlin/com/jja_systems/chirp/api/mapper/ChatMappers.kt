package com.jja_systems.chirp.api.mapper

import com.jja_systems.chirp.api.dto.ChatDto
import com.jja_systems.chirp.api.dto.ChatMessageDto
import com.jja_systems.chirp.api.dto.ChatParticipantDto
import com.jja_systems.chirp.domain.model.Chat
import com.jja_systems.chirp.domain.model.ChatMessage
import com.jja_systems.chirp.domain.model.ChatParticipant

fun Chat.toDto(): ChatDto {
    return ChatDto(
        id = id,
        participants = participants.map {
            it.toDto()
        },
        lastActivityAt = lastActivityAt,
        lastMessage = lastMessage?.toDto(),
        creator = creator.toDto()
    )
}

fun ChatMessage.toDto(): ChatMessageDto {
    return ChatMessageDto(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = createdAt,
        senderId = sender.userId
    )
}

fun ChatParticipant.toDto(): ChatParticipantDto {
    return ChatParticipantDto(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}