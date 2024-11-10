package com.api.pulley.web.dto.response

import com.api.pulley.domain.unitCode.UnitCode

data class UnitCodeResponse(
    val unitCode: String,
    val name: String
) {
     companion object{
         fun UnitCode.toResponse(): UnitCodeResponse =
             UnitCodeResponse(
                 unitCode = unitCode,
                 name = name
             )
     }
}
