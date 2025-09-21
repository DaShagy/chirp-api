package com.juanjoseabuin.chirp.infra.message_queue

import com.juanjoseabuin.chirp.domain.event.user.UserEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class NotificationIUserEventListener {

    @RabbitListener(
        queues = [MessageQueues.NOTIFICATION_USER_EVENTS]
    )
    @Transactional
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Created -> {
                println("UserCreated")
            }
            is UserEvent.RequestResendVerification -> {
                println("RequestResendVerification")
            }
            is UserEvent.RequestResetPassword -> {
                println("RequestResetPassword")
            }
            else -> Unit
        }
    }
}