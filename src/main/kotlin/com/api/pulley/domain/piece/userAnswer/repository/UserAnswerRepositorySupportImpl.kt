package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.QPiece
import com.api.pulley.domain.piece.problemPiece.QProblemPiece
import com.api.pulley.domain.piece.userAnswer.QUserAnswer
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.problem.QProblem
import com.api.pulley.domain.unitCode.QUnitCode
import com.api.pulley.domain.unitCode.UnitCode
import com.api.pulley.domain.user.User
import com.api.pulley.internal.MarkResultType
import com.api.pulley.internal.Utils.toPercentage
import com.api.pulley.web.dto.response.PieceAnalyzeResponse
import com.api.pulley.web.dto.response.PieceResponse.Companion.toResponse
import com.api.pulley.web.dto.response.ProblemStatisticsResponse
import com.api.pulley.web.dto.response.ProblemResponse.Companion.toResponse
import com.api.pulley.web.dto.response.PieceStatisticsResponse
import com.api.pulley.web.dto.response.UserResponse.Companion.toResponse
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import java.math.BigDecimal

class UserAnswerRepositorySupportImpl(
    private val query: JPAQueryFactory,
): UserAnswerRepositorySupport {
    override fun analyze(pieceId: Long): PieceAnalyzeResponse {
        val piece = getPiece(pieceId)
        val totalProblemCount = countProblems(pieceId)
        val userPieces = getUserPieces(pieceId, totalProblemCount)
        val problemPieces = getProblemPieces(pieceId)

        return PieceAnalyzeResponse(
            piece = piece.toResponse(),
            users = userPieces.map { it.user },
            userPieces = userPieces,
            problemPieces = problemPieces
        )
    }

    private fun getPiece(pieceId: Long): Piece {
        return query
            .selectFrom(QPiece.piece)
            .where(QPiece.piece.id.eq(pieceId))
            .fetchOne() ?: throw NoSuchElementException("not found piece")
    }

    private fun getUserPieces(pieceId: Long, totalProblemCount: Long): List<PieceStatisticsResponse> {
        data class UserStats(
            val user: User,
            val passCount: Number,
            val totalCount: Number,
        )

        return query
            .select(
                Projections.constructor(
                    UserStats::class.java,
                    QUserAnswer.userAnswer.user,
                    QUserAnswer.userAnswer.markResultType.`when`(MarkResultType.PASS).then(1).otherwise(0).sum(),
                    QUserAnswer.userAnswer.count()
                )
            )
            .from(QUserAnswer.userAnswer)
            .where(QUserAnswer.userAnswer.piece.id.eq(pieceId))
            .groupBy(QUserAnswer.userAnswer.user)
            .fetch()
            .map { stats ->
                PieceStatisticsResponse(
                    user = stats.user.toResponse(),
                    passRate = stats.passCount.toPercentage(totalProblemCount),
                    totalRate = stats.totalCount.toPercentage(totalProblemCount),
                    notSolvedCount = totalProblemCount.toInt() - stats.totalCount.toInt()
                )
            }
    }
    private fun getProblemPieces(pieceId: Long): List<ProblemStatisticsResponse> {
        data class ProblemStats(
            val problem: Problem,
            val unitCode: UnitCode,
            val passCount: Number,
            val totalCount: Number
        )

        return query
            .select(
                Projections.constructor(
                    ProblemStats::class.java,
                    QProblem.problem,
                    QProblem.problem.unitCode,
                    QUserAnswer.userAnswer.markResultType.`when`(MarkResultType.PASS).then(1).otherwise(0).sum(),
                    QUserAnswer.userAnswer.count()
                )
            )
            .from(QProblemPiece.problemPiece)
            .join(QProblemPiece.problemPiece.problem, QProblem.problem)
            .join(QProblem.problem.unitCode, QUnitCode.unitCode1)
            .leftJoin(QUserAnswer.userAnswer)
            .on(
                QUserAnswer.userAnswer.problem.eq(QProblem.problem),
                QUserAnswer.userAnswer.piece.id.eq(pieceId)
            )
            .where(QProblemPiece.problemPiece.piece.id.eq(pieceId))
            .groupBy(QProblem.problem)
            .fetch()
            .map { stats ->
                ProblemStatisticsResponse(
                    problem = stats.problem.toResponse(),
                    passRate = if(stats.totalCount.toInt() > 0)
                        stats.passCount.toInt().toBigDecimal()
                            .toPercentage(stats.totalCount.toInt().toBigDecimal())
                    else BigDecimal.ZERO
                )
            }
    }
    private fun countProblems(pieceId: Long): Long {
        return query
            .select(QProblemPiece.problemPiece.count())
            .from(QProblemPiece.problemPiece)
            .where(QProblemPiece.problemPiece.piece.id.eq(pieceId))
            .fetchOne() ?: 0L
    }

}