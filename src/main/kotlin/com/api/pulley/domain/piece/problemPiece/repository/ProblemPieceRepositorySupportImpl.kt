package com.api.pulley.domain.piece.problemPiece.repository

import com.api.pulley.domain.piece.problemPiece.QProblemPiece
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.problem.QProblem
import com.querydsl.jpa.impl.JPAQueryFactory

class ProblemPieceRepositorySupportImpl(
    private val query: JPAQueryFactory
): ProblemPieceRepositorySupport {
    override fun findProblems(pieceId: Long): List<Problem> {
        return query
            .select(QProblem.problem)
            .from(QProblemPiece.problemPiece)
            .leftJoin(QProblemPiece.problemPiece.problem, QProblem.problem)
            .leftJoin(QProblem.problem.unitCode).fetchJoin()
            .where(QProblemPiece.problemPiece.piece.id.eq(pieceId))
            .fetch()
    }


}