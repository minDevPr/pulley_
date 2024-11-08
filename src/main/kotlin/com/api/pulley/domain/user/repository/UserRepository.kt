package com.api.pulley.domain.user.repository

import com.api.pulley.domain.user.User
import com.api.pulley.internal.RoleType
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByRoleType(roleType: RoleType): List<User>
    fun findAllByIdIn(ids: Collection<Long>): List<User>
}