package com.api.pulley.domain.piece.repository


import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.QPiece
import com.api.pulley.domain.piece.problemPiece.QProblemPiece
import com.api.pulley.domain.problem.QProblem
import com.api.pulley.domain.unitCode.QUnitCode
import com.querydsl.jpa.impl.JPAQueryFactory

class PieceRepositorySupportImpl(
    private val query: JPAQueryFactory,
): PieceRepositorySupport {

}