package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserAnswerRepository: JpaRepository<UserAnswer, Long>, UserAnswerRepositorySupport {
    fun findAllByUserAndPieceAndProblemIn(user: User, piece: Piece, problems: Collection<Problem>): List<UserAnswer>
}