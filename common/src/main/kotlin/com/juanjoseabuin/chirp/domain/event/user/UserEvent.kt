package com.juanjoseabuin.chirp.domain.event.user

import com.juanjoseabuin.chirp.domain.event.ChirpEvent
import com.juanjoseabuin.chirp.domain.type.UserId
import java.time.Instant
import java.util.UUID

sealed class UserEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val exchange: String = UserEventsConstants.USER_EXCHANGE,
    override val occurredAt: Instant = Instant.now()
): ChirpEvent {

    data class Created(
        val userId: UserId,
        val email: String,
        val username: String,
        val verificationToken: String,
        override val eventKey: String = UserEventsConstants.USER_CREATED_KEY
    ) : UserEvent(), ChirpEvent

    data class Verified(
        val userId: UserId,
        val email: String,
        val username: String,
        override val eventKey: String = UserEventsConstants.USER_VERIFIED_KEY
    ) : UserEvent(), ChirpEvent

    data class RequestResendVerification(
        val userId: UserId,
        val email: String,
        val username: String,
        val verificationToken: String,
        override val eventKey: String = UserEventsConstants.USER_REQUEST_RESEND_VERIFICATION_KEY
    ) : UserEvent(), ChirpEvent

    data class RequestResetPassword(
        val userId: UserId,
        val email: String,
        val username: String,
        val passwordResetToken: String,
        val expiresInMinutes: Long,
        override val eventKey: String = UserEventsConstants.USER_REQUEST_RESET_PASSWORD_KEY
    ) : UserEvent(), ChirpEvent
}