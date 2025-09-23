package com.juanjoseabuin.chirp.api.mapper

import com.juanjoseabuin.chirp.api.dto.ChatDto
import com.juanjoseabuin.chirp.api.dto.ChatMessageDto
import com.juanjoseabuin.chirp.api.dto.ChatParticipantDto
import com.juanjoseabuin.chirp.domain.model.Chat
import com.juanjoseabuin.chirp.domain.model.ChatMessage
import com.juanjoseabuin.chirp.domain.model.ChatParticipant

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