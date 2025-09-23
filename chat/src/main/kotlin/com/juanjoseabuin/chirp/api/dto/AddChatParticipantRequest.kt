package com.juanjoseabuin.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.juanjoseabuin.chirp.domain.type.UserId
import jakarta.validation.constraints.Size

data class AddChatParticipantRequest(
    @field:Size(min = 1)
    @JsonProperty("userIds")
    val userIds: List<UserId>
)
