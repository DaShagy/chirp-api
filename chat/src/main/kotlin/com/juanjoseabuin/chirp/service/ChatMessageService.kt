package com.juanjoseabuin.chirp.service

import com.juanjoseabuin.chirp.api.dto.ChatMessageDto
import com.juanjoseabuin.chirp.api.mapper.toDto
import com.juanjoseabuin.chirp.domain.exception.ChatMessageNotFoundException
import com.juanjoseabuin.chirp.domain.exception.ChatNotFoundException
import com.juanjoseabuin.chirp.domain.exception.ChatParticipantNotFoundException
import com.juanjoseabuin.chirp.domain.exception.ForbiddenException
import com.juanjoseabuin.chirp.domain.model.ChatMessage
import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.domain.type.ChatMessageId
import com.juanjoseabuin.chirp.domain.type.UserId
import com.juanjoseabuin.chirp.infra.database.entity.ChatMessageEntity
import com.juanjoseabuin.chirp.infra.database.mapper.toChatMessage
import com.juanjoseabuin.chirp.infra.database.repository.ChatMessageRepository
import com.juanjoseabuin.chirp.infra.database.repository.ChatParticipantRepository
import com.juanjoseabuin.chirp.infra.database.repository.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository
) {

    fun getChatMessages(
        chatId: ChatId,
        before: Instant?,
        pageSize: Int
    ): List<ChatMessageDto> {
        return chatMessageRepository
            .findByChatIdBefore(
                chatId = chatId,
                before = before ?: Instant.now(),
                pageable = PageRequest.of(0, pageSize)
            )
            .content
            .asReversed()
            .map { it.toChatMessage().toDto() }
    }

    @Transactional
    fun sendMessage(
        chatId: ChatId,
        senderId: UserId,
        content: String,
        messageId: ChatMessageId? = null
    ): ChatMessage {
        val chat = chatRepository.findChatById(chatId, senderId)
            ?: throw ChatNotFoundException()

        val sender = chatParticipantRepository.findByIdOrNull(senderId)
            ?: throw ChatParticipantNotFoundException(senderId)

        val savedMessage = chatMessageRepository.save(
            ChatMessageEntity(
                id = messageId,
                content = content,
                chatId = chatId,
                chat = chat,
                sender = sender
            )
        )

        return savedMessage.toChatMessage()
    }

    @Transactional
    fun deleteMessage(
        messageId: ChatMessageId,
        requestUserId: UserId
    ) {
        val message = chatMessageRepository.findByIdOrNull(messageId)
            ?: throw ChatMessageNotFoundException(messageId)

        if(message.sender.userId != requestUserId) {
            throw ForbiddenException()
        }

        chatMessageRepository.delete(message)
    }
}