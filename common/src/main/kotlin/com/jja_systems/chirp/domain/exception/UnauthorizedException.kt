package com.jja_systems.chirp.domain.exception

import java.lang.RuntimeException

class UnauthorizedException() : RuntimeException(
    "Missing auth details"
)