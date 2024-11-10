package com.api.pulley.domain.piece.problemPiece.repository

import com.api.pulley.domain.problem.Problem

interface ProblemPieceRepositorySupport {
    fun findProblems(pieceId: Long): List<Problem>
}