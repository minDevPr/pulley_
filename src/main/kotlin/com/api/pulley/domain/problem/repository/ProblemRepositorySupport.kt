package com.api.pulley.domain.problem.repository

import com.api.pulley.domain.problem.Problem
import com.api.pulley.internal.LevelType
import com.api.pulley.internal.ProblemType

interface ProblemRepositorySupport {
    fun get(totalCount: Int, unitCodes: List<String>, levelType: LevelType, problemType: ProblemType) : List<Problem>

}