package com.api.pulley.web.dto.request


data class PieceMarkRequests(
    val userId: Long,
    val marks: List<PieceMarkRequest>,
)



data class PieceMarkRequest(
    val problemId: Long,
    val answer: Int,
)
