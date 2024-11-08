package com.api.pulley.domain.piece.userPiece

import com.api.pulley.domain.base.IdAuditEntity
import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.user.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class UserPiece(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id")
    val piece: Piece,
): IdAuditEntity() {
}