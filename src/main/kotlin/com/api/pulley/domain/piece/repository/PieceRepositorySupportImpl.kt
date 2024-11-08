package com.api.pulley.domain.piece.repository


import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.QPiece
import com.api.pulley.domain.piece.problemPiece.QProblemPiece.problemPiece

import com.api.pulley.domain.problem.QProblem.problem
import com.api.pulley.domain.unitCode.QUnitCode
import com.querydsl.jpa.impl.JPAQueryFactory

class PieceRepositorySupportImpl(
    private val query: JPAQueryFactory,
): PieceRepositorySupport {
    override fun findProblems(pieceId: Long): Piece? {
        return query
            .selectFrom(QPiece.piece)
            .leftJoin(QPiece.piece.problems, problemPiece).fetchJoin()
            .leftJoin(problemPiece.problem, problem).fetchJoin()
            .leftJoin(problem.unitCode, QUnitCode.unitCode1).fetchJoin()
            .where(QPiece.piece.id.eq(QPiece.piece.id))
            .fetchOne()
    }


}