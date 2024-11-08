package com.api.pulley.service

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.domain.piece.userAnswer.repository.UserAnswerRepository
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.user.User
import com.api.pulley.internal.MarkResultType
import org.springframework.stereotype.Service

@Service
class UserAnswerService(
    private val userAnswerRepository: UserAnswerRepository,
) {
    fun save(user: User,
             piece: Piece,
             problem: Problem,
             userAnswer: Int,
             )
    : UserAnswer {
        return UserAnswer(
            user = user,
            piece = piece,
            problem = problem,
            answer = userAnswer,
            markResultType = if (problem.answer == userAnswer) MarkResultType.PASS else MarkResultType.FAIL
        ).run {
            userAnswerRepository.save(this)
        }
    }

    fun saveAll(
        user: User,
        piece: Piece,
        problemRequests: Map<Problem, Int>
    ): List<UserAnswer> {
        val userAnswers = problemRequests.map { (problem, userAnswer) ->
            UserAnswer(
                user = user,
                piece = piece,
                problem = problem,
                answer = userAnswer,
                markResultType = if (problem.answer == userAnswer)
                    MarkResultType.PASS
                else
                    MarkResultType.FAIL
            )
        }
        return userAnswerRepository.saveAll(userAnswers)
    }
}