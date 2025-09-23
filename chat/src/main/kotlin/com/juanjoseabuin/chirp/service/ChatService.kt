package com.juanjoseabuin.chirp.service

import com.juanjoseabuin.chirp.domain.exception.ChatParticipantNotFoundException
import com.juanjoseabuin.chirp.domain.exception.InvalidChatSizeException
import com.juanjoseabuin.chirp.domain.model.Chat
import com.juanjoseabuin.chirp.domain.type.UserId
import com.juanjoseabuin.chirp.infra.database.entity.ChatEntity
import com.juanjoseabuin.chirp.infra.database.mapper.toChat
import com.juanjoseabuin.chirp.infra.database.repository.ChatParticipantRepository
import com.juanjoseabuin.chirp.infra.database.repository.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository
) {

    @Transactional
    fun createChat(
        creatorId: UserId,
        otherUserIds: Set<UserId>
    ): Chat {
        val otherParticipants = chatParticipantRepository.findByUserIdIn(
            userIds = otherUserIds
        )

        val allParticipants = (otherParticipants + creatorId)

        if (allParticipants.size < 2) {
            throw InvalidChatSizeException()
        }

        val creator = chatParticipantRepository.findByIdOrNull(creatorId)
            ?: throw ChatParticipantNotFoundException(creatorId)

        return chatRepository.save(
            ChatEntity(
                creator = creator,
                participants = setOf(creator) + otherParticipants,
            )
        ).toChat(lastMessage = null)
    }
}