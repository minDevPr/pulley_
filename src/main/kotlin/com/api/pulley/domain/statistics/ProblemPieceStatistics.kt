package com.api.pulley.domain.statistics

import com.api.pulley.domain.base.IdEntity
import com.api.pulley.domain.piece.problemPiece.ProblemPiece
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class ProblemPieceStatistics(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_piece_id")
    val problemPiece: ProblemPiece,
    var attemptCount: Int = 0,
    var passCount: Int = 0,
    var passRate: Double
): IdEntity() {
}