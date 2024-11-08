package com.api.pulley.domain.unitCode.repository

import com.api.pulley.domain.unitCode.UnitCode
import org.springframework.data.jpa.repository.JpaRepository

interface UnitCodeRepository: JpaRepository<UnitCode,String> {
}