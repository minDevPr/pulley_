package com.api.pulley.web.api

import com.api.pulley.internal.LevelType
import com.api.pulley.internal.ProblemType
import com.api.pulley.service.ProblemService
import com.api.pulley.web.dto.response.ProblemResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems")
class ProblemController(
    private val problemService: ProblemService,
) {
    @GetMapping
    fun readAll(
        @RequestParam totalCount: Int,
        @RequestParam unitCodeList: List<String>,
        @RequestParam level: LevelType,
        @RequestParam problemType: ProblemType,
    ): List<ProblemResponse> {
        return problemService.readAll(totalCount,unitCodeList,level,problemType)
    }
}