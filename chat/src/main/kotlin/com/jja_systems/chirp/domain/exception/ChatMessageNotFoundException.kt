package com.jja_systems.chirp.domain.exception

import com.jja_systems.chirp.domain.type.ChatMessageId
import java.lang.RuntimeException

class ChatMessageNotFoundException(id: ChatMessageId): RuntimeException(
    "Message with ID $id not found"
)