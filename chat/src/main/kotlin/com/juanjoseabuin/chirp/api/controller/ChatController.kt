package com.juanjoseabuin.chirp.api.controller

import com.juanjoseabuin.chirp.api.dto.AddChatParticipantRequest
import com.juanjoseabuin.chirp.api.dto.ChatDto
import com.juanjoseabuin.chirp.api.dto.CreateChatRequest
import com.juanjoseabuin.chirp.api.mapper.toDto
import com.juanjoseabuin.chirp.api.util.requestUserId
import com.juanjoseabuin.chirp.domain.type.ChatId
import com.juanjoseabuin.chirp.service.ChatService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService
) {

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