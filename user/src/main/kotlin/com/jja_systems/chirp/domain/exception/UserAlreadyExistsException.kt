package com.jja_systems.chirp.domain.exception

class UserAlreadyExistsException: RuntimeException(
    "A user with this username or email already exists."
)