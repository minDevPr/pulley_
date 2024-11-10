package com.api.pulley.web.dto.response

data class UserPieceResponse(
    val piece: PieceResponse,
    val users: List<UserResponse>
)
