package com.api.pulley.web.dto.response

import com.api.pulley.domain.user.User
import java.util.NoSuchElementException

data class UserResponse(
    val id: Long,
    val name: String
) {
     companion object{
         fun User.toResponse(): UserResponse =
             UserResponse(
                 id = id ?: throw NoSuchElementException("not found user"),
                 name = name
             )
     }
}
