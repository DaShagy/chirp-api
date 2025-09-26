package com.jja_systems.chirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.jja_systems.chirp.domain.type.UserId
import jakarta.validation.constraints.Size

data class AddChatParticipantRequest(
    @field:Size(min = 1)
    @JsonProperty("userIds")
    val userIds: List<UserId>
)
