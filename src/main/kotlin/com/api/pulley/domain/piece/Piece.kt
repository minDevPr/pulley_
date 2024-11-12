package com.api.pulley.domain.piece

import com.api.pulley.domain.base.IdAuditEntity
import com.api.pulley.domain.piece.problemPiece.ProblemPiece
import com.api.pulley.domain.user.User
import com.api.pulley.domain.piece.userPiece.UserPiece
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Piece(
    val name: String,
    @ManyToOne val user: User,
): IdAuditEntity() {
    @OneToMany(mappedBy = "piece") val users: MutableList<UserPiece> = mutableListOf()
    @OneToMany(mappedBy = "piece") val problems: MutableList<ProblemPiece> = mutableListOf()
}