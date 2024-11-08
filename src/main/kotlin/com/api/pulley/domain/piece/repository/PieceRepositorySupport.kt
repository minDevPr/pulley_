package com.api.pulley.domain.piece.repository

import com.api.pulley.domain.piece.Piece

interface PieceRepositorySupport {
    fun findProblems(piece: Long): Piece?
}