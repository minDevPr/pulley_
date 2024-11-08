package com.api.pulley.domain.piece.problemPiece

import com.api.pulley.domain.base.IdAuditEntity
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.piece.Piece
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ProblemPiece(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    val problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id")
    val piece: Piece,

    ): IdAuditEntity() {
}