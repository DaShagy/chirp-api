package com.jja_systems.chirp.infra.database.message_queue

import com.jja_systems.chirp.domain.event.user.UserEvent
import com.jja_systems.chirp.domain.model.ChatParticipant
import com.jja_systems.chirp.infra.message_queue.MessageQueues
import com.jja_systems.chirp.service.ChatParticipantService
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