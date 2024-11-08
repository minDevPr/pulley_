package com.api.pulley.domain.statistics

import com.api.pulley.domain.base.IdEntity
import com.api.pulley.domain.piece.userPiece.UserPiece
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne

@Entity
class UserPieceStatistics(
    @OneToOne(fetch = FetchType.LAZY)
    val userPiece: UserPiece,
    var passCount: Int = 0,
    var totalProblems: Int,
    var passRate: Double
) : IdEntity() {
}