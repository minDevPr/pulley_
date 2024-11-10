package com.api.pulley.domain.piece.userPiece.repository

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.user.User
import com.api.pulley.domain.piece.userPiece.UserPiece
import org.springframework.data.jpa.repository.JpaRepository

interface UserPieceRepository: JpaRepository<UserPiece,Long> {
    fun findAllByPiece(piece: Piece): List<UserPiece>
    fun existsByUserAndPiece(user: User, piece: Piece): Boolean

    fun findByUserAndPiece(user:User, piece: Piece): UserPiece

}