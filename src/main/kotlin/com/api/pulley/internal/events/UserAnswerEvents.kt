package com.api.pulley.internal.events

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.user.User
import com.api.pulley.internal.MarkResultType


data class UserAnswerEvents(
    val user :User,
    val piece: Piece,
    val contents: List<UserAnswerEvent>
)

data class UserAnswerEvent(
    val problem: Problem,
    val markResultType: MarkResultType,
)
