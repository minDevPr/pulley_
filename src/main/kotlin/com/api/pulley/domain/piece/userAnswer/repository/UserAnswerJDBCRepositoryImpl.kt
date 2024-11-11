package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.internal.Utils
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserAnswerJDBCRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
): UserAnswerJDBCRepository {
    override fun saveAllBatch(newAnswers: List<UserAnswer>): List<UserAnswer> {
        if (newAnswers.isEmpty()) return emptyList()

        val insertSql = "INSERT INTO user_answer (answer, mark_result_type, piece_id, problem_id, user_id) VALUES (?, ?, ?, ?, ?)"
        val insertArgs = newAnswers.map { userAnswer ->
            arrayOf(
                userAnswer.answer,
                userAnswer.markResultType.name,
                userAnswer.piece.id,
                userAnswer.problem.id,
                userAnswer.user.id
            )
        }
        jdbcTemplate.batchUpdate(insertSql, insertArgs)

        return newAnswers
    }

    override fun updateAllBatch(updateAnswers: List<Utils.UserAnswerUpdateDto>): List<UserAnswer> {
        if (updateAnswers.isEmpty()) return emptyList()
        val updateSql = "UPDATE user_answer SET answer = ?, mark_result_type = ? WHERE piece_id = ? AND problem_id = ? AND user_id = ?"
        val updateArgs = updateAnswers.map { userAnswer ->
            arrayOf(
                userAnswer.answer,
                userAnswer.markResultType.name,
                userAnswer.piece.id,
                userAnswer.problem.id,
                userAnswer.user.id
            )
        }
        jdbcTemplate.batchUpdate(updateSql, updateArgs)

        return emptyList()
    }
}