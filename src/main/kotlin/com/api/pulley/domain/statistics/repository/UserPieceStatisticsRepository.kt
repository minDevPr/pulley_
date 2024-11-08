package com.api.pulley.domain.statistics.repository

import com.api.pulley.domain.statistics.UserPieceStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface UserPieceStatisticsRepository: JpaRepository<UserPieceStatistics,Long> {
}