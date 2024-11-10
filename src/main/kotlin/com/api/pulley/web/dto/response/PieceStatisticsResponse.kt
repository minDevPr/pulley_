package com.api.pulley.web.dto.response

import java.math.BigDecimal

data class PieceStatisticsResponse(
    val user: UserResponse,
    val passRate: BigDecimal, // 정답률
    val totalRate: BigDecimal, // 문제 푼 비율
    val notSolvedCount: Int, // 안푼 문제 수
)
