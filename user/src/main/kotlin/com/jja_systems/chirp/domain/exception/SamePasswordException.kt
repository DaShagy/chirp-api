package com.jja_systems.chirp.domain.exception

class SamePasswordException: RuntimeException(
    "The new password can not be equal to the old password"
)