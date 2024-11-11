package com.api.pulley.internal

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.user.User
import java.math.BigDecimal
import java.math.RoundingMode


object Utils {
    object LevelRanges {
        val LOW_RANGE = 1..1
        val MIDDLE_RANGE = 2..4
        val HIGH_RANGE = 5..5
    }

    fun Number.toPercentage(total: Number, scale: Int = 4): BigDecimal =
        this.toRate(total, scale).multiply(BigDecimal(100))

    private fun Number.toRate(total: Number, scale: Int = 4): BigDecimal =
        BigDecimal(this.toString())
            .divide(BigDecimal(total.toString()), scale, RoundingMode.HALF_UP)

    data class UserAnswerUpdateDto(
        val piece: Piece,
        val problem: Problem,
        val user: User,
        val answer: Int,
        val markResultType: MarkResultType
    )

}