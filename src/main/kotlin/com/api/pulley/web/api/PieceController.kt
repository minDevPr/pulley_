package com.api.pulley.web.api

import com.api.pulley.service.PieceService
import com.api.pulley.service.UserPieceService
import com.api.pulley.web.dto.request.UserPieceCreateRequest
import com.api.pulley.web.dto.request.PieceCreateRequest
import com.api.pulley.web.dto.request.PieceMarkRequest
import com.api.pulley.web.dto.response.MarkResultResponse
import com.api.pulley.web.dto.response.PieceAnalyzeResponse
import com.api.pulley.web.dto.response.ProblemPieceResponse
import com.api.pulley.web.dto.response.ProblemResponse
import com.api.pulley.web.dto.response.UserPieceResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/piece")
class PieceController(
    private val pieceService: PieceService,
    private val userPieceService: UserPieceService,
) {
    @PostMapping
    fun save(
        @RequestParam userId: Long,
        @RequestBody request: PieceCreateRequest,
    ): ProblemPieceResponse {
        return pieceService.save(userId, request)
    }

    @PostMapping("{pieceId}")
    fun exam(
        @PathVariable pieceId: Long,
        @RequestBody request: UserPieceCreateRequest
    ): UserPieceResponse {
       return userPieceService.save(request.studentIds,pieceId)
    }

    @GetMapping("problems/{pieceId}")
    fun read(
        @PathVariable pieceId: Long,
        @RequestParam userId: Long,
    ): List<ProblemResponse>? {
        return pieceService.read(userId,pieceId)
    }

    @PutMapping("problems/{pieceId}")
    fun mark(
        @PathVariable pieceId: Long,
        @RequestParam userId: Long,
        @RequestBody request: List<PieceMarkRequest>,
    ): List<MarkResultResponse>{
        return pieceService.mark(userId,pieceId,request)
    }

    @GetMapping("analyze/{pieceId}")
    fun analyze(
        @PathVariable pieceId: Long,
    ): PieceAnalyzeResponse {
        return pieceService.analyze(pieceId)
    }

}