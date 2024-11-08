package com.api.pulley.service

import com.api.pulley.domain.piece.Piece
import com.api.pulley.domain.piece.userPiece.repository.UserPieceRepository
import com.api.pulley.domain.piece.repository.PieceRepository
import com.api.pulley.domain.user.repository.UserRepository
import com.api.pulley.domain.piece.userPiece.UserPiece
import com.api.pulley.domain.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserPieceService(
    private val pieceRepository: PieceRepository,
    private val userPieceRepository: UserPieceRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun save(userIds: List<Long>, pieceId: Long) {
        val piece = pieceRepository.findById(pieceId).orElseThrow()
        val userPieceMap = userPieceRepository.findAllByPiece(piece).associateBy { it.user.id }

        userRepository.findAllByIdIn(userIds)
            .filterNot { userPieceMap.containsKey(it.id) }
            .map { UserPiece(user = it, piece = piece) }
            .run { userPieceRepository.saveAll(this) }
    }

    fun valid(piece: Piece, user: User): Boolean {
        return userPieceRepository.existsByUserAndPiece(user,piece)
    }
}