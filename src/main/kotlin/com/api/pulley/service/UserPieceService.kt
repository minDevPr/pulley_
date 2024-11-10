package com.api.pulley.service

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.userPiece.repository.UserPieceRepository
import com.api.pulley.domain.piece.repository.PieceRepository
import com.api.pulley.domain.user.repository.UserRepository
import com.api.pulley.domain.piece.userPiece.UserPiece
import com.api.pulley.domain.user.User
import com.api.pulley.web.dto.response.PieceResponse.Companion.toResponse
import com.api.pulley.web.dto.response.UserPieceResponse
import com.api.pulley.web.dto.response.UserResponse.Companion.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserPieceService(
    private val pieceRepository: PieceRepository,
    private val userPieceRepository: UserPieceRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun save(userIds: List<Long>, pieceId: Long): UserPieceResponse {
        val piece = pieceRepository.findById(pieceId).orElseThrow()
        val userPieceMap = userPieceRepository.findAllByPiece(piece).associateBy { it.user.id }

        val userPieces = userRepository.findAllByIdIn(userIds)
            .filterNot { userPieceMap.containsKey(it.id) }
            .map { UserPiece(user = it, piece = piece) }
            .run { userPieceRepository.saveAll(this) }

        return UserPieceResponse(
            piece = piece.toResponse(),
            users = userPieces.plus(userPieceMap.values).map { it.user.toResponse() }
        )
    }

    fun valid(piece: Piece, user: User): Boolean {
        return userPieceRepository.existsByUserAndPiece(user,piece)
    }
}