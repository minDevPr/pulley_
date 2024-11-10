package com.api.pulley.service

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.repository.PieceRepository
import com.api.pulley.domain.problem.repository.ProblemRepository
import com.api.pulley.domain.piece.problemPiece.ProblemPiece
import com.api.pulley.domain.piece.problemPiece.repository.ProblemPieceRepository
import com.api.pulley.domain.piece.userAnswer.repository.UserAnswerRepository
import com.api.pulley.domain.user.User
import com.api.pulley.domain.user.repository.UserRepository
import com.api.pulley.web.dto.request.PieceCreateRequest
import com.api.pulley.web.dto.request.PieceMarkRequest
import com.api.pulley.web.dto.response.MarkResultResponse
import com.api.pulley.web.dto.response.MarkResultResponse.Companion.toResponse
import com.api.pulley.web.dto.response.PieceAnalyzeResponse
import com.api.pulley.web.dto.response.PieceResponse.Companion.toResponse
import com.api.pulley.web.dto.response.ProblemPieceResponse
import com.api.pulley.web.dto.response.ProblemResponse
import com.api.pulley.web.dto.response.ProblemResponse.Companion.toResponse
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PieceService(
    private val userRepository: UserRepository,
    private val pieceRepository: PieceRepository,
    private val problemRepository: ProblemRepository,
    private val problemPieceRepository: ProblemPieceRepository,
    private val userPieceService: UserPieceService,
    private val userAnswerService: UserAnswerService,
    private val userAnswerRepository: UserAnswerRepository,
) {

    @Transactional(readOnly = true)
    fun read(userId: Long, pieceId: Long): List<ProblemResponse>? {
        val user = userRepository.findById(userId).orElseThrow()
        val piece = pieceRepository.findById(pieceId).orElseThrow()

        if(!userPieceService.valid(piece,user)) return throw BadRequestException("not valid")

        return problemPieceRepository.findProblems(pieceId).map { it.toResponse() }
    }

    @Transactional
    fun save(userId: Long, request: PieceCreateRequest): ProblemPieceResponse {
        val user = userRepository.findById(userId).orElseThrow()
        val piece = save(user,request.name)

        val problemPieces = problemRepository.findAllByIdIn(request.problemIds)
            .map { ProblemPiece(piece= piece, problem = it) }
            .run { problemPieceRepository.saveAll(this) }

        return ProblemPieceResponse(
            piece = piece.toResponse(),
            problems = problemPieces.map { it.problem.toResponse() }
        )
    }

    @Transactional
    fun mark(userId: Long, pieceId: Long, requests: List<PieceMarkRequest>): List<MarkResultResponse> {
        val user = userRepository.findById(userId).orElseThrow()
        val piece = pieceRepository.findById(pieceId).orElseThrow()

        if (!userPieceService.valid(piece, user)) {
            throw BadRequestException("not valid")
        }

        val problems = problemRepository.findAllByIdIn(requests.map { it.problemId })
            .associateBy { it.id }

        val problemRequests = requests.mapNotNull { request ->
            problems[request.problemId]?.let { problem ->
                problem to request.answer
            }
        }.toMap()

        return userAnswerService.saveAll(user, piece, problemRequests)
            .map { it.toResponse() }
    }

    fun analyze(pieceId: Long): PieceAnalyzeResponse {
        return userAnswerRepository.analyze(pieceId)
    }

    private fun save(user: User, name:String): Piece {
        return pieceRepository.save(Piece(name, user))
    }
}