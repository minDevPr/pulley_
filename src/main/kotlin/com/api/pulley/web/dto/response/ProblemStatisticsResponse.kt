package com.api.pulley.web.dto.response

import java.math.BigDecimal

data class ProblemStatisticsResponse(
    val problem: ProblemResponse,
    val passRate: BigDecimal,
    val totalRate: BigDecimal
)
