package com.juanjoseabuin.chirp.api.controller

import com.juanjoseabuin.chirp.api.dto.AddChatParticipantRequest
import com.juanjoseabuin.chirp.api.dto.ChatDto
import com.juanjoseabuin.chirp.api.dto.ChatMessageDto
import com.juanjoseabuin.chirp.api.dto.CreateChatRequest
import com.juanjoseabuin.chirp.api.mapper.toDto
import com.juanjoseabuin.chirp.api.util.requestUserId
import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.service.ChatMessageService
import com.juanjoseabuin.chirp.service.ChatService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
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