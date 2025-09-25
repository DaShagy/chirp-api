package com.juanjoseabuin.chirp.infra.message_queue

import com.juanjoseabuin.chirp.domain.event.chat.ChatEvent
import com.juanjoseabuin.chirp.domain.event.user.UserEvent
import com.juanjoseabuin.chirp.service.EmailService
import com.juanjoseabuin.chirp.service.PushNotificationService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

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