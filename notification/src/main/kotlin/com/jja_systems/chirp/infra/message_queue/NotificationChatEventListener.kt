package com.jja_systems.chirp.infra.message_queue

import com.jja_systems.chirp.domain.event.chat.ChatEvent
import com.jja_systems.chirp.service.PushNotificationService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class NotificationChatEventListener(
    private val pushNotificationService: PushNotificationService
) {

    @RabbitListener(
        queues = [MessageQueues.NOTIFICATION_CHAT_EVENTS]
    )
    @Transactional
    fun handleUserEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.NewMessage -> {
                pushNotificationService.sendNewMessageNotification(
                    recipientUserIds = event.recipientIds.toList(),
                    senderUserId = event.senderId,
                    senderUsername = event.senderUsername,
                    message = event.message,
                    chatId = event.chatId
                )
            }
        }
    }
}