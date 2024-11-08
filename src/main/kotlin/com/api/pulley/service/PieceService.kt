package com.api.pulley.service

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.repository.PieceRepository
import com.api.pulley.domain.problem.Problem
import com.api.pulley.domain.problem.repository.ProblemRepository
import com.api.pulley.domain.piece.problemPiece.ProblemPiece
import com.api.pulley.domain.piece.problemPiece.repository.ProblemPieceRepository
import com.api.pulley.domain.piece.userAnswer.UserAnswer
import com.api.pulley.domain.user.User
import com.api.pulley.domain.user.repository.UserRepository
import com.api.pulley.web.dto.request.PieceCreateRequest
import com.api.pulley.web.dto.request.PieceMarkRequest
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
) {

    @Transactional(readOnly = true)
    fun get(userId: Long, pieceId: Long): List<Problem>{
        val user = userRepository.findById(userId).orElseThrow()
        val piece = pieceRepository.findById(pieceId).orElseThrow()

        if(!userPieceService.valid(piece,user)) return throw BadRequestException("not valid")

        return pieceRepository.findProblems(pieceId)
            ?.problems
            ?.map { it.problem }
            ?: throw BadRequestException("Piece not found")
    }

    @Transactional
    fun save(userId: Long, request: PieceCreateRequest) {
        val user = userRepository.findById(userId).orElseThrow()
        val piece = save(user,request.name)

        problemRepository.findAllByIdIn(request.problemIds)
            .map { ProblemPiece(piece= piece, problem = it) }
            .run { problemPieceRepository.saveAll(this) }
    }


    @Transactional
    fun mark(userId: Long, pieceId: Long, requests: List<PieceMarkRequest>): List<UserAnswer> {
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
    }


    fun analyze() {
        TODO("고민 해 보기")
    }


    private fun save(user: User, name:String): Piece {
        return pieceRepository.save(Piece(name, user))
    }
}