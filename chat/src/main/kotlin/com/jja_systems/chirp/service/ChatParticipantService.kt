package com.jja_systems.chirp.service

import com.jja_systems.chirp.domain.model.ChatParticipant
import com.jja_systems.chirp.domain.type.UserId
import com.jja_systems.chirp.infra.database.mapper.toChatParticipant
import com.jja_systems.chirp.infra.database.mapper.toEntity
import com.jja_systems.chirp.infra.database.repository.ChatParticipantRepository
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