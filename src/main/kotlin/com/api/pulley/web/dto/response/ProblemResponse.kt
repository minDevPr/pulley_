package com.api.pulley.web.dto.response

import com.api.pulley.domain.problem.Problem
import com.api.pulley.internal.ProblemType
import com.api.pulley.web.dto.response.UnitCodeResponse.Companion.toResponse
import java.util.NoSuchElementException

data class ProblemResponse(
    val id: Long,
    val unitCode: UnitCodeResponse,
    val level: Int,
    val answer: Int,
    val problemType: ProblemType,
) {
    companion object{
        fun Problem.toResponse(): ProblemResponse =
            ProblemResponse(
                id = id ?: throw NoSuchElementException("Not found problem"),
                unitCode = unitCode.toResponse(),
                level = level,
                answer = answer,
                problemType = problemType,
            )


    }
}
