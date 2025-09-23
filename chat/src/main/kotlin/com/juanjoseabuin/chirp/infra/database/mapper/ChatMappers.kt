package com.juanjoseabuin.chirp.infra.database.mapper

import com.juanjoseabuin.chirp.domain.model.Chat
import com.juanjoseabuin.chirp.domain.model.ChatMessage
import com.juanjoseabuin.chirp.domain.model.ChatParticipant
import com.juanjoseabuin.chirp.infra.database.entity.ChatEntity
import com.juanjoseabuin.chirp.infra.database.entity.ChatMessageEntity
import com.juanjoseabuin.chirp.infra.database.entity.ChatParticipantEntity

fun ChatEntity.toChat(lastMessage: ChatMessage? = null): Chat {
    return Chat(
        id = id!!,
        participants = participants.map {
            it.toChatParticipant()
        }.toSet(),
        creator = creator.toChatParticipant(),
        lastMessage = lastMessage,
        lastActivityAt = lastMessage?.createdAt ?: createdAt,
        createdAt = createdAt
    )
}

fun ChatParticipantEntity.toChatParticipant(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id!!,
        chatId = chatId,
        sender = sender.toChatParticipant(),
        content = content,
        createdAt = createdAt
    )
}

fun ChatParticipant.toEntity(): ChatParticipantEntity {
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        email = email,
        profilePictureUrl = profilePictureUrl
    )
}