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
    // @PersistenceContext
    // private lateinit var entityManager: EntityManager

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

    // val batchedResults = result.chunked(50).flatMap { batch ->
    //     val savedBatch = userAnswerRepository.saveAll(batch)
    //     entityManager.flush()
    //     entityManager.clear()
    //     savedBatch
    // }

    private fun marked(problem: Problem, userAnswer: Int): MarkResultType {
        return if (problem.answer == userAnswer) MarkResultType.PASS else MarkResultType.FAIL
    }
}