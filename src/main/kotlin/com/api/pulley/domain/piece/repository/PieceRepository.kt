package com.api.pulley.domain.piece.repository

import com.api.pulley.domain.piece.Piece
import org.springframework.data.jpa.repository.JpaRepository

interface PieceRepository: JpaRepository<Piece, Long>,PieceRepositorySupport {
}