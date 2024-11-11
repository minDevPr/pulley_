package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.internal.Utils

interface UserAnswerJDBCRepository {
    fun saveAllBatch(entities: List<UserAnswer>): List<UserAnswer>
    fun updateAllBatch(entities: List<Utils.UserAnswerUpdateDto>): List<UserAnswer>
}