package com.api.pulley.service

import com.api.pulley.domain.problem.repository.ProblemRepository
import com.api.pulley.internal.LevelType
import com.api.pulley.internal.ProblemType
import com.api.pulley.web.dto.response.ProblemResponse
import com.api.pulley.web.dto.response.ProblemResponse.Companion.toResponse
import org.springframework.stereotype.Service

@Service
class ProblemService(
    private val problemRepository: ProblemRepository,
) {
    fun readAll(totalCount: Int,
               unitCodeList: List<String>,
               levelType: LevelType,
               problemType: ProblemType,
    ): List<ProblemResponse>{
        return problemRepository.get(totalCount,unitCodeList,levelType,problemType)
            .map { it.toResponse() }
    }
}