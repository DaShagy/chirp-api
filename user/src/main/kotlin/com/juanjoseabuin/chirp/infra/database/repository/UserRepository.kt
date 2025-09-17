package com.juanjoseabuin.chirp.infra.database.repository

import com.juanjoseabuin.chirp.domain.model.UserId
import com.juanjoseabuin.chirp.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}