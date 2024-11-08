package com.api.pulley.web.dto.request

data class PieceCreateRequest(
    val problemIds: Set<Long>,
    val name: String,
)
