package com.juanjoseabuin.chirp.domain.exception

import java.lang.RuntimeException

class ChatNotFoundException(): RuntimeException(
    "Chat not found"
)