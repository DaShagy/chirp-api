package com.juanjoseabuin.chirp.service

import com.juanjoseabuin.chirp.domain.model.ChatParticipant
import com.juanjoseabuin.chirp.domain.type.UserId
import com.juanjoseabuin.chirp.infra.database.mapper.toChatParticipant
import com.juanjoseabuin.chirp.infra.database.mapper.toEntity
import com.juanjoseabuin.chirp.infra.database.repository.ChatParticipantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatParticipantService(
    private val chatParticipantRepository: ChatParticipantRepository
) {

    fun createChatParticipant(
        chatParticipant: ChatParticipant
    ) {
        chatParticipantRepository.save(
            chatParticipant.toEntity()
        )
    }

    fun findChatParticipantById(
        userId: UserId
    ) : ChatParticipant? {
        return chatParticipantRepository.findByIdOrNull(userId)?.toChatParticipant()
    }

    fun findChatParticipantByEmailOrUsername(
        query: String
    ) : ChatParticipant? {
        val normalizedQuery = query.lowercase().trim()
        return chatParticipantRepository.findByEmailOrUsername(normalizedQuery)?.toChatParticipant()
    }
}