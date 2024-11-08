package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.domain.piece.userAnswer.UserAnswer
import org.springframework.data.jpa.repository.JpaRepository

interface UserAnswerRepository: JpaRepository<UserAnswer, Long> {
}