package com.juanjoseabuin.chirp.infra.database.message_queue

import com.juanjoseabuin.chirp.domain.event.user.UserEvent
import com.juanjoseabuin.chirp.domain.model.ChatParticipant
import com.juanjoseabuin.chirp.infra.message_queue.MessageQueues
import com.juanjoseabuin.chirp.service.ChatParticipantService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatUserEventListener(
    private val chatParticipantService: ChatParticipantService
) {

    @RabbitListener(
        queues = [MessageQueues.CHAT_USER_EVENTS]
    )
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Verified -> {
                chatParticipantService.createChatParticipant(
                    chatParticipant = ChatParticipant(
                        userId = event.userId,
                        username = event.username,
                        email = event.email,
                        profilePictureUrl = null
                    )
                )
            }
            else -> Unit
        }
    }
}