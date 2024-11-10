package com.api.pulley.domain.piece.userAnswer.repository

import com.api.pulley.web.dto.response.PieceAnalyzeResponse

interface UserAnswerRepositorySupport {

    fun analyze(pieceId: Long): PieceAnalyzeResponse
}