package com.api.pulley.domain.piece.userAnswer

import com.api.pulley.domain.base.IdAuditEntity
import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.user.User
import com.api.pulley.internal.MarkResultType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne

@Entity
class UserAnswer(
    @ManyToOne(fetch= FetchType.LAZY)
    val user: User,

    @ManyToOne(fetch= FetchType.LAZY)
    val problem: Problem,

    @ManyToOne(fetch= FetchType.LAZY)
    val piece: Piece,

    val answer: Int,

    @Enumerated(EnumType.STRING) val markResultType: MarkResultType,
): IdAuditEntity() {
}