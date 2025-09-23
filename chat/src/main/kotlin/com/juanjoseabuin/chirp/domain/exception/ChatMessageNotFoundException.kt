package com.juanjoseabuin.chirp.domain.exception

import com.juanjoseabuin.chirp.domain.type.ChatMessageId
import java.lang.RuntimeException

class ChatMessageNotFoundException(id: ChatMessageId): RuntimeException(
    "Message with ID $id not found"
)