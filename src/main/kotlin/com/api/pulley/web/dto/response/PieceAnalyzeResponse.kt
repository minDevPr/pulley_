package com.api.pulley.web.dto.response

data class PieceAnalyzeResponse(
    val piece: PieceResponse,
    val users: List<UserResponse>,
    val userPieces: List<PieceStatisticsResponse>,
    val problemPieces: List<ProblemStatisticsResponse>
)
