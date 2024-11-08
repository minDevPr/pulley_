package com.api.pulley.domain.user

import com.api.pulley.domain.base.IdAuditEntity
import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.userPiece.UserPiece
import com.api.pulley.internal.RoleType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Enumerated(EnumType.STRING) val roleType: RoleType,
    val name: String,
): IdAuditEntity() {
    @OneToMany(mappedBy = "user")
    val pieces: MutableList<Piece> = mutableListOf()

    @OneToMany(mappedBy = "user")
    val userPieces: MutableList<UserPiece> = mutableListOf()
}