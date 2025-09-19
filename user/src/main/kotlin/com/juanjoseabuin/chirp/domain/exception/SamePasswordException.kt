package com.juanjoseabuin.chirp.domain.exception

class SamePasswordException: RuntimeException(
    "The new password can not be equal to the old password"
)