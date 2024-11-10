package com.api.pulley.web.dto.response

import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.internal.MarkResultType
import com.api.pulley.web.dto.response.ProblemResponse.Companion.toResponse

data class MarkResultResponse(
    val problem: ProblemResponse,
    val markResultType: MarkResultType
){
    companion object{
        fun UserAnswer.toResponse(): MarkResultResponse =
            MarkResultResponse(
                problem = problem.toResponse(),
                markResultType = markResultType
            )
    }
}
