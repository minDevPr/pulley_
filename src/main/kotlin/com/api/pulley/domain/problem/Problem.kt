package com.api.pulley.domain.problem

import com.api.pulley.domain.base.IdEntity
import com.api.pulley.domain.piece.problemPiece.ProblemPiece
import com.api.pulley.domain.unitCode.UnitCode
import com.api.pulley.internal.ProblemType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@NamedEntityGraph(
    name = "Problem.withUnitCode",
    attributeNodes = [
        NamedAttributeNode("unitCode")
    ]
)
@Table(
    indexes = [
        Index(
            name = "idx_problem_unit_code",
            columnList = "unit_code"
        ),
    ]
)
class Problem(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_code")
    val unitCode: UnitCode,
    val level: Int,
    @Enumerated(EnumType.STRING) val problemType: ProblemType,
    val answer: Int,
) : IdEntity() {
    @OneToMany(mappedBy = "problem")
    val pieces: MutableList<ProblemPiece> = mutableListOf()
}