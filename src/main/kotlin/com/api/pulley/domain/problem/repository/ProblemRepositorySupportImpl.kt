package com.api.pulley.domain.problem.repository

import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.problem.QProblem.problem
import com.api.pulley.internal.LevelType
import com.api.pulley.internal.ProblemType
import com.querydsl.jpa.impl.JPAQueryFactory

class ProblemRepositorySupportImpl(
    private val query: JPAQueryFactory,
): ProblemRepositorySupport {
    override fun get(
        totalCount: Int,
        unitCodes: List<String>,
        levelType: LevelType,
        problemType: ProblemType,
    ): List<Problem> {
        val (low, middle, high) = levelType.toRate(totalCount)
        println("Low: ${low.second}, Middle: ${middle.second}, High: ${high.second}")

        return buildList {
            listOf(low, middle, high).forEach { (range, count) ->
                addAll(
                    query.selectFrom(problem)
                        .where(
                            problem.unitCode.unitCode.`in`(unitCodes),
                            if (problemType != ProblemType.ALL) problem.problemType.eq(problemType) else null,
                            problem.level.between(range.first, range.last),
                        )
                        .limit(count)
                        .fetch()
                )
            }
        }
    }
}