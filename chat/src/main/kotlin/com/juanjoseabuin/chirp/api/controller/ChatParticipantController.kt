package com.juanjoseabuin.chirp.api.controller

import com.juanjoseabuin.chirp.api.dto.ChatParticipantDto
import com.juanjoseabuin.chirp.api.dto.ConfirmProfilePictureRequest
import com.juanjoseabuin.chirp.api.dto.PictureUploadDto
import com.juanjoseabuin.chirp.api.mapper.toDto
import com.juanjoseabuin.chirp.api.util.requestUserId
import com.juanjoseabuin.chirp.service.ChatParticipantService
import com.juanjoseabuin.chirp.service.ProfilePictureService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/participants")
class ChatParticipantController(
    private val chatParticipantService: ChatParticipantService,
    private val profilePictureService: ProfilePictureService
) {

    @GetMapping
    fun getChatParticipantByEmailOrUsername(
        @RequestParam(required = false) query: String?
    ): ChatParticipantDto {
        val participant = if (query == null) {
            chatParticipantService.findChatParticipantById(userId = requestUserId)
        } else {
            chatParticipantService.findChatParticipantByEmailOrUsername(query)
        }

        return participant?.toDto() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/profile-picture-upload")
    fun getProfilePictureUploadUrl(
        @RequestParam mimeType: String,
    ): PictureUploadDto {
        return profilePictureService.generateUploadCredentials(
            userId = requestUserId,
            mimeType = mimeType
        ).toDto()
    }

    @PostMapping("/confirm-profile-picture")
    fun confirmProfilePictureUpload(
        @Valid @RequestBody body: ConfirmProfilePictureRequest
    ) {
        profilePictureService.confirmProfilePictureUpload(
            userId = requestUserId,
            publicUrl = body.publicUrl
        )
    }

    @DeleteMapping("/profile-picture")
    fun deleteProfilePicture() {
        profilePictureService.deleteProfilePicture(
            userId = requestUserId
        )
    }
}