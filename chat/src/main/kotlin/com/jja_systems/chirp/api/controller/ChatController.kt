package com.jja_systems.chirp.api.controller

import com.jja_systems.chirp.api.dto.AddChatParticipantRequest
import com.jja_systems.chirp.api.dto.ChatDto
import com.jja_systems.chirp.api.dto.ChatMessageDto
import com.jja_systems.chirp.api.dto.CreateChatRequest
import com.jja_systems.chirp.api.mapper.toDto
import com.jja_systems.chirp.api.util.requestUserId
import com.jja_systems.chirp.domain.type.ChatId
import com.jja_systems.chirp.service.ChatService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

    @GetMapping("/{chatId}")
    fun getChat(
        @PathVariable("chatId") chatId: ChatId
    ): ChatDto {
        return chatService.getChatById(
            chatId = chatId,
            requestUserId = requestUserId
        )?.toDto() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{chatId}/messages")
    fun getMessagesForChat(
        @PathVariable("chatId") chatId: ChatId,
        @RequestParam("before", required = false) before: Instant? = null,
        @RequestParam("pageSize", required = false) pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<ChatMessageDto> {
        return chatService.getChatMessages(
            chatId = chatId,
            before = before,
            pageSize = pageSize
        )
    }

    @GetMapping
    fun getChatsForUser(): List<ChatDto> {
        return chatService.findChatsByUser(
            userId = requestUserId
        ).map { it.toDto() }
    }

    @PostMapping
    fun createChat(
        @Valid @RequestBody body: CreateChatRequest
    ): ChatDto {
         return chatService.createChat(
             creatorId = requestUserId,
             otherUserIds = body.otherUserIds.toSet()
         ).toDto()
    }

    @PostMapping("/{chatId}/add")
    fun addChatParticipants(
        @PathVariable chatId: ChatId,
        @Valid @RequestBody body: AddChatParticipantRequest
    ): ChatDto {
        return chatService.addParticipantsToChat(
            requestUserId = requestUserId,
            chatId = chatId,
            userIds = body.userIds.toSet()
        ).toDto()
    }

    @DeleteMapping("/{chatId}/leave")
    fun leaveChat(
        @PathVariable chatId: ChatId
    ) {
        return chatService.removeParticipantFromChat(
            chatId = chatId,
            userId = requestUserId
        )
    }
}