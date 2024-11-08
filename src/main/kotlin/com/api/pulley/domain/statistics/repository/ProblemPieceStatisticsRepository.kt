package com.api.pulley.domain.statistics.repository

import com.api.pulley.domain.statistics.ProblemPieceStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemPieceStatisticsRepository: JpaRepository<ProblemPieceStatistics,Long> {
}