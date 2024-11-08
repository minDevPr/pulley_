package com.api.pulley.domain.unitCode

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class UnitCode(
    @Id val unitCode: String,
    val name: String,
)