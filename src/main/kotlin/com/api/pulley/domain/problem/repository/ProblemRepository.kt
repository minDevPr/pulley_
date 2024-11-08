package com.api.pulley.domain.problem.repository

import com.api.pulley.domain.problem.Problem
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepository: JpaRepository<Problem,Long>, ProblemRepositorySupport {
    @EntityGraph(value = "Problem.withUnitCode")
    override fun findAll(): List<Problem>
    @EntityGraph(value = "Problem.withUnitCode")
    fun findAllByIdIn(ids: Collection<Long>): Collection<Problem>
}