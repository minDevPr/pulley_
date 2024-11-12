package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.QPiece
import com.api.pulley.domain.piece.problemPiece.QProblemPiece
import com.api.pulley.domain.piece.userAnswer.QUserAnswer
import com.api.pulley.domain.piece.userPiece.QUserPiece
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.problem.QProblem
import com.api.pulley.domain.unitCode.QUnitCode
import com.api.pulley.domain.unitCode.UnitCode
import com.api.pulley.domain.user.QUser
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
        val totalUsersCount = getTotalUsersCount(pieceId)
        val totalProblemCount = countProblems(pieceId)
        val userPieces = getUserPieces(pieceId, totalProblemCount)
        val problemPieces = getProblemPieces(pieceId, totalUsersCount)

        return PieceAnalyzeResponse(
            piece = piece.toResponse(),
            users = piece.users.map { it.user.toResponse() },
            userPieces = userPieces,
            problemPieces = problemPieces
        )
    }

    private fun getPiece(pieceId: Long): Piece {
        return query
            .selectFrom(QPiece.piece)
            .leftJoin(QPiece.piece.users, QUserPiece.userPiece).fetchJoin()
            .leftJoin(QUserPiece.userPiece.user, QUser.user).fetchJoin()
            .where(QPiece.piece.id.eq(pieceId))
            .fetchOne() ?: throw NoSuchElementException("Piece not found")
    }

    private fun getTotalUsersCount(pieceId: Long): Long {
        return query
            .select(QUserPiece.userPiece.count())
            .from(QUserPiece.userPiece)
            .where(QUserPiece.userPiece.piece.id.eq(pieceId))
            .fetchOne() ?: 0L
    }

    private fun getUserPieces(pieceId: Long, totalProblemCount: Long): List<PieceStatisticsResponse> {
        data class UserStats(
            val user: User,
            val passCount: Number,
            val totalCount: Number
        )

        return query
            .select(
                Projections.constructor(
                    UserStats::class.java,
                    QUserPiece.userPiece.user,
                    QUserAnswer.userAnswer.markResultType.`when`(MarkResultType.PASS).then(1).otherwise(0).sum(),
                    QUserAnswer.userAnswer.count()
                )
            )
            .from(QUserPiece.userPiece)
            .leftJoin(QUserAnswer.userAnswer)
            .on(
                QUserAnswer.userAnswer.user.eq(QUserPiece.userPiece.user),
                QUserAnswer.userAnswer.piece.id.eq(pieceId)
            )
            .where(QUserPiece.userPiece.piece.id.eq(pieceId))
            .groupBy(QUserPiece.userPiece.user)
            .fetch()
            .map { stats ->
                val actualTotalSolvedCount = stats.totalCount.toInt()
                PieceStatisticsResponse(
                    user = stats.user.toResponse(),
                    passRate = stats.passCount.toPercentage(totalProblemCount),
                    totalRate = actualTotalSolvedCount.toBigDecimal().toPercentage(totalProblemCount.toBigDecimal()),
                    notSolvedCount = totalProblemCount.toInt() - actualTotalSolvedCount
                )
            }
    }


    private fun getProblemPieces(pieceId: Long, totalUsersCount: Long): List<ProblemStatisticsResponse> {
        data class ProblemStats(val problem: Problem, val unitCode: UnitCode, val passCount: Number, val totalCount: Number)

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
                val solveRate = if (totalUsersCount > 0) {
                    stats.totalCount.toInt().toBigDecimal().toPercentage(totalUsersCount.toBigDecimal())
                } else BigDecimal.ZERO

                ProblemStatisticsResponse(
                    problem = stats.problem.toResponse(),
                    passRate = if (stats.totalCount.toInt() > 0)
                        stats.passCount.toInt().toBigDecimal().toPercentage(stats.totalCount.toInt().toBigDecimal())
                    else BigDecimal.ZERO,
                    totalRate = solveRate
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