package com.jja_systems.chirp.domain.exception

import java.lang.RuntimeException

class InvalidProfilePictureException(
    override val message: String? = null
): RuntimeException(
    message ?: "Invalid profile picture data"
)