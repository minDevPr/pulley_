package com.api.pulley.domain.piece.problemPiece.repository

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.problemPiece.ProblemPiece
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemPieceRepository: JpaRepository<ProblemPiece,Long>, ProblemPieceRepositorySupport {

    fun findByPiece(piece: Piece): List<ProblemPiece>
}