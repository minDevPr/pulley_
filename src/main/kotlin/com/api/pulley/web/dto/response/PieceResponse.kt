package com.api.pulley.web.dto.response

import com.api.pulley.domain.piece.Piece
import com.api.pulley.web.dto.response.UserResponse.Companion.toResponse
import java.util.NoSuchElementException

data class PieceResponse(
    val id: Long,
    val name: String,
    val user: UserResponse,
) {
     companion object{
         fun Piece.toResponse(): PieceResponse =
             PieceResponse(
                 id = id ?: throw NoSuchElementException("not found piece"),
                 name = name,
                 user = user.toResponse()
             )
     }
}
