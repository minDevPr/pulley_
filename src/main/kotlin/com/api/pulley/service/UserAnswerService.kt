package com.api.pulley.service

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.domain.piece.userAnswer.repository.UserAnswerJDBCRepository
import com.api.pulley.domain.piece.userAnswer.repository.UserAnswerRepository
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.user.User
import com.api.pulley.internal.MarkResultType
import com.api.pulley.internal.Utils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAnswerService(
    private val userAnswerRepository: UserAnswerRepository,
    private val userAnswerJDBCRepository: UserAnswerJDBCRepository
) {

    fun save(user: User, piece: Piece, problem: Problem, userAnswer: Int): UserAnswer {
        return UserAnswer(
            user = user,
            piece = piece,
            problem = problem,
            answer = userAnswer,
            markResultType = marked(problem,userAnswer)
        )
    }

    fun saveAll(
        user: User,
        piece: Piece,
        problemRequests: Map<Problem, Int>
    ): List<UserAnswer> {
        val userAnswers = userAnswerRepository.findAllByUserAndPieceAndProblemIn(user, piece, problemRequests.keys)

        val result = problemRequests.map { (problem, userAnswer) ->
            userAnswers.find { it.problem == problem }
                ?.let { it.update(userAnswer, marked(problem,userAnswer)) }
                ?: save(user, piece, problem, userAnswer)
        }
        return userAnswerRepository.saveAll(result)
    }

    @Transactional
    fun saveAllBatch(
        user: User,
        piece: Piece,
        problemRequests: Map<Problem, Int>
    ): List<UserAnswer> {

        val userAnswers = userAnswerRepository.findAllByUserAndPieceAndProblemIn(user, piece, problemRequests.keys)

        val newAnswers = mutableListOf<UserAnswer>()
        val updatedAnswers = mutableListOf<Utils.UserAnswerUpdateDto>()

        problemRequests.map { (problem, userAnswer) ->
            userAnswers.find { it.problem == problem }
                ?.let {
                    updatedAnswers.add(
                        Utils.UserAnswerUpdateDto(
                            piece = piece,
                            problem = problem,
                            user = user,
                            answer = userAnswer,
                            markResultType = marked(problem, userAnswer)
                        )
                    )
                 }
                ?: save(user, piece, problem, userAnswer)
                    .also { newAnswers.add(it) }
        }

        if (newAnswers.isNotEmpty()) {
            newAnswers.chunked(50).flatMap {
                userAnswerJDBCRepository.saveAllBatch(newAnswers)
            }
        }

        if (updatedAnswers.isNotEmpty()) {
            updatedAnswers.chunked(50).flatMap {
                userAnswerJDBCRepository.updateAllBatch(updatedAnswers)
            }
        }

        return userAnswerRepository.findAllByUserAndPieceAndProblemIn(user, piece, problemRequests.keys)
    }


    private fun marked(problem: Problem, userAnswer: Int): MarkResultType {
        return if (problem.answer == userAnswer) MarkResultType.PASS else MarkResultType.FAIL
    }
}