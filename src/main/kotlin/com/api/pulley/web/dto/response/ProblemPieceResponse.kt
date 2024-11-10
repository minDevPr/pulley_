package com.api.pulley.web.dto.response

data class ProblemPieceResponse(
    val piece: PieceResponse,
    val problems: List<ProblemResponse>
)