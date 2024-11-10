package com.api.pulley.domain.problem.repository

import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.problem.QProblem
import com.api.pulley.domain.unitCode.QUnitCode
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
        println("low: ${low.second}, middle: ${middle.second}, high: ${high.second}")

        return buildList {
            listOf(low, middle, high).forEach { (range, count) ->
                addAll(
                    query.selectFrom(QProblem.problem)
                        .where(
                            QProblem.problem.unitCode.unitCode.`in`(unitCodes),
                            if (problemType != ProblemType.ALL) QProblem.problem.problemType.eq(problemType) else null,
                            QProblem.problem.level.between(range.first, range.last),
                        )
                        .leftJoin(QProblem.problem.unitCode, QUnitCode.unitCode1).fetchJoin()
                        .limit(count)
                        .fetch()
                )
            }
        }
    }
}