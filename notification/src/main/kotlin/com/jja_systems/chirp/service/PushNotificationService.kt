package com.jja_systems.chirp.service

import com.jja_systems.chirp.domain.exception.InvalidDeviceTokenException
import com.jja_systems.chirp.domain.model.DeviceToken
import com.jja_systems.chirp.domain.model.PushNotification
import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.domain.type.UserId
import com.jja_systems.chirp.infra.database.DeviceTokenEntity
import com.jja_systems.chirp.infra.database.DeviceTokenRepository
import com.jja_systems.chirp.infra.database.mappers.toDeviceToken
import com.jja_systems.chirp.infra.database.mappers.toPlatformEntity
import com.jja_systems.chirp.infra.push_notification.FirebasePushNotificationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentSkipListMap

@Service
class PushNotificationService(
    private val deviceTokenRepository: DeviceTokenRepository,
    private val firebasePushNotificationService: FirebasePushNotificationService
) {

    companion object {
        private val RETRY_DELAYS_SECONDS = listOf(
            30L,
            60L,
            120L,
            300L,
            600L
        )

        const val MAX_RETRY_AGE_MINUTES = 30L
    }

    private val retryQueue: ConcurrentSkipListMap<Long, MutableList<RetryData>> = ConcurrentSkipListMap()

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun registerDevice(
        userId: UserId,
        token: String,
        platform: DeviceToken.Platform
    ): DeviceToken {
        val existing = deviceTokenRepository.findByToken(token)

        val trimmedToken = token.trim()
        if (existing == null && !firebasePushNotificationService.isValidToken(token)) {
            throw InvalidDeviceTokenException()
        }

        val entity = if(existing != null) {
            deviceTokenRepository.save(
                existing.apply {
                    this.userId = userId
                }
            )
        } else {
            deviceTokenRepository.save(
                DeviceTokenEntity(
                    userId = userId,
                    token = trimmedToken,
                    platform = platform.toPlatformEntity()
                )
            )
        }

        return entity.toDeviceToken()
    }

    @Transactional
    fun unregisterDevice(token: String) {
        deviceTokenRepository.deleteByToken(token.trim())
    }

    fun sendNewMessageNotification(
        recipientUserIds: List<UserId>,
        senderUserId: UserId,
        senderUsername: String,
        message: String,
        chatId: ChatId
    ) {
        val deviceTokens = deviceTokenRepository.findByUserIdIn(recipientUserIds)

        if (deviceTokens.isEmpty()) {
            logger.info("No device tokens found for $recipientUserIds")
            return
        }

        val recipients = deviceTokens
            .filter { it.userId != senderUserId }
            .map { it.toDeviceToken() }

        val notification = PushNotification(
            title = "New message fromm $senderUsername",
            recipients = recipients,
            message = message,
            chatId = chatId,
            data = mapOf(
                "chatId" to chatId.toString(),
                "type" to "new_message"
            )
        )

        sendWithRetry(notification)
    }

    @Transactional
    fun sendWithRetry(
        notification: PushNotification,
        attempt: Int = 0
    ) {
        val result = firebasePushNotificationService.sendNotification(notification)

        result.permanentFailures.forEach {
            deviceTokenRepository.deleteByToken(it.token)
        }


        if (result.temporaryFailures.isEmpty() && attempt < RETRY_DELAYS_SECONDS.size) {
            val retryNotification = notification.copy(recipients = result.temporaryFailures)
            scheduleRetry(retryNotification, attempt + 1)
        }

        if (result.succeeded.isNotEmpty()) {
            logger.info("Successfully sent notification to ${result.succeeded.size} devices")
        }
    }

    private fun scheduleRetry(
        notification: PushNotification,
        attempt: Int
    ) {
        val delay = RETRY_DELAYS_SECONDS.getOrElse(attempt - 1) {
            RETRY_DELAYS_SECONDS.last()
        }
        val executeAt = Instant.now().plusSeconds(delay)
        val executeAtMillis = executeAt.toEpochMilli()

        val retryData = RetryData(
            notification = notification,
            attempt = attempt,
            createAt = Instant.now()
        )

        retryQueue.compute(executeAtMillis) { _, retries ->
            (retries ?: mutableListOf()).apply { add(retryData) }
        }

        logger.info("Scheduled retry $attempt for ${notification.id} in $delay seconds")
    }

    @Scheduled(fixedDelay = 15_000L)
    fun processRetries() {
        val now = Instant.now()
        val nowMillis = now.toEpochMilli()

        val toProcess = retryQueue.headMap(nowMillis, true)

        if (toProcess.isEmpty()) {
            return
        }

        val entries = toProcess.entries.toList()

        entries.forEach { (timeMills, retries) ->
            retryQueue.remove(timeMills)

            retries.forEach { retry ->
                try {
                    val age = Duration.between(retry.createAt, now)
                    if (age.toMinutes() > MAX_RETRY_AGE_MINUTES) {
                        logger.warn("Dropping old retry (${age.toMinutes()} old)")
                        return@forEach
                    }

                    sendWithRetry(
                        notification = retry.notification,
                        attempt = retry.attempt
                    )
                } catch (e: Exception) {
                    logger.warn("Error processing retry ${retry.notification.id}", e)
                }
            }
        }
    }

    private data class RetryData(
        val notification: PushNotification,
        val attempt: Int,
        val createAt: Instant
    )
}