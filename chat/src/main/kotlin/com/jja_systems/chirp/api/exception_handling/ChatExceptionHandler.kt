package com.jja_systems.chirp.api.exception_handling

import com.jja_systems.chirp.domain.exception.ChatMessageNotFoundException
import com.jja_systems.chirp.domain.exception.ChatNotFoundException
import com.jja_systems.chirp.domain.exception.ChatParticipantNotFoundException
import com.jja_systems.chirp.domain.exception.InvalidChatSizeException
import com.jja_systems.chirp.domain.exception.InvalidProfilePictureException
import com.jja_systems.chirp.domain.exception.StorageException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ChatExceptionHandler {

    @ExceptionHandler(
        ChatNotFoundException::class,
        ChatMessageNotFoundException::class,
        ChatParticipantNotFoundException::class
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onNotFound(
        e: Exception
    ) = mapOf(
        "code" to "NOT_FOUND",
        "message" to e.message
    )

    @ExceptionHandler(InvalidChatSizeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onInvalidChatSize(e: InvalidChatSizeException) = mapOf(
        "code" to "INVALID_CHAT_SIZE",
        "message" to e.message
    )

    @ExceptionHandler(InvalidProfilePictureException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onInvalidProfilePicture(e: InvalidProfilePictureException) = mapOf(
        "code" to "INVALID_PROFILE_PICTURE",
        "message" to e.message
    )

    @ExceptionHandler(StorageException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun onInvalidStorage(e: StorageException) = mapOf(
        "code" to "STORAGE_ERROR",
        "message" to e.message
    )
}