package com.jja_systems.chirp.infra.database.repository

import com.jja_systems.chirp.domain.type.UserId
import com.jja_systems.chirp.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): List<UserEntity>
}