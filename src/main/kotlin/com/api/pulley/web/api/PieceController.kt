package com.api.pulley.web.api

import com.api.pulley.service.PieceService
import com.api.pulley.service.UserPieceService
import com.api.pulley.web.dto.request.UserPieceCreateRequest
import com.api.pulley.web.dto.request.PieceCreateRequest
import com.api.pulley.web.dto.request.PieceMarkRequest
import com.api.pulley.web.dto.request.PieceMarkRequests
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
        @RequestBody request: PieceCreateRequest,
    ): ProblemPieceResponse {
        return pieceService.save(request.userId, request)
    }

    @PostMapping("{pieceId}")
    fun exam(
        @PathVariable pieceId: Long,
        @RequestBody request: UserPieceCreateRequest
    ): UserPieceResponse {
       return userPieceService.save(request.studentIds,pieceId)
    }

    @GetMapping("{pieceId}/problems")
    fun read(
        @PathVariable pieceId: Long,
    ): List<ProblemResponse>? {
        return pieceService.read(pieceId)
    }

    @GetMapping("{pieceId}/problems")
    fun mark(
        @PathVariable pieceId: Long,
        @RequestBody request: PieceMarkRequests
    ): List<MarkResultResponse>{
        return pieceService.mark(request.userId, pieceId, request.marks)
    }

    @GetMapping("{pieceId}/analyze/")
    fun analyze(
        @PathVariable pieceId: Long,
    ): PieceAnalyzeResponse {
        return pieceService.analyze(pieceId)
    }

}