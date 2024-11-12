package com.api.pulley.web.dto.request

data class PieceCreateRequest(
    val userId : Long,
    val problemIds: Set<Long>,
    val name: String,
)
