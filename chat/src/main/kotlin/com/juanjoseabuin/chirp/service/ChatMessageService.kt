package com.juanjoseabuin.chirp.service

import com.juanjoseabuin.chirp.api.dto.ChatMessageDto
import com.juanjoseabuin.chirp.api.mapper.toDto
import com.juanjoseabuin.chirp.domain.event.MessageDeletedEvent
import com.juanjoseabuin.chirp.domain.event.chat.ChatEvent
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
import com.juanjoseabuin.chirp.infra.message_queue.EventPublisher
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val eventPublisher: EventPublisher,
    private val messageCacheEvictionHelper: MessageCacheEvictionHelper
) {

    @Transactional
    @CacheEvict(
        value = ["messages"],
        key = "#chatId"
    )
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

        val savedMessage = chatMessageRepository.saveAndFlush(
            ChatMessageEntity(
                id = messageId ?: UUID.randomUUID(),
                content = content,
                chatId = chatId,
                chat = chat,
                sender = sender
            )
        )

        eventPublisher.publish(
            event = ChatEvent.NewMessage(
                senderId = sender.userId,
                senderUsername = sender.username,
                recipientIds = chat.participants.map { it.userId }.toSet(),
                chatId = chatId,
                message = savedMessage.content
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

        applicationEventPublisher.publishEvent(
            MessageDeletedEvent(
                messageId = messageId,
                chatId = message.chatId
            )
        )

        messageCacheEvictionHelper.evictMessagesCache(message.chatId)
    }
}