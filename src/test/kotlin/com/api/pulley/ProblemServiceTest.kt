package com.api.pulley

import com.api.pulley.domain.piece.problemPiece.repository.ProblemPieceRepository
import com.api.pulley.domain.piece.repository.PieceRepository
import com.api.pulley.domain.problem.repository.ProblemRepository
import com.api.pulley.domain.unitCode.repository.UnitCodeRepository
import com.api.pulley.domain.user.repository.UserRepository
import com.api.pulley.internal.LevelType
import com.api.pulley.internal.ProblemType
import com.api.pulley.internal.RoleType
import com.api.pulley.service.UserPieceService
import com.api.pulley.service.PieceService
import com.api.pulley.service.ProblemService
import com.api.pulley.web.dto.request.PieceCreateRequest
import com.api.pulley.web.dto.request.PieceMarkRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProblemServiceTest(
    @Autowired private val problemService: ProblemService,
    @Autowired private val unitCodeRepository: UnitCodeRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val pieceService: PieceService,
    @Autowired private val problemRepository: ProblemRepository,
    @Autowired private val examService: UserPieceService,
    @Autowired private val pieceRepository: PieceRepository,
    @Autowired private val problemPieceRepository: ProblemPieceRepository,
) {

    @Test
    fun problemGet() {
        val result = problemService.readAll(totalCount = 10,
            unitCodeList = listOf("uc1580","uc1540","uc1583","uc1564","uc1535","uc1578","uc1541","uc1573","uc1576"),
            levelType = LevelType.MIDDLE,
            problemType = ProblemType.ALL
        )

        // - 상 선택시 : **하** 문제 20%, **중** 문제 30%, **상** 문제 50%
        // - 중 선택시 : **하** 문제 25%, **중** 문제 50%, **상** 문제 25%
        // - 하 선택시 : **하** 문제 50%, **중** 문제 30%, **상** 문제 20%

        result.map {
            println(it.unitCode.name)
            println(it.level)
            println("-------------------------")
        }
        println(result.size)
    }

    @Test
    fun save() {
        val user = userRepository.findByRoleType(RoleType.TEACHER).last()
        val problems = problemRepository.findAll()
            .shuffled()
            .take(50)

        val request = PieceCreateRequest(
            problemIds = problems.map { it.id!! }.toSet(),
            name = "문제를 풀어보아요"
        )
        pieceService.save(user.id!!, request = request)
    }

    @Test
    fun exam(){
        val user = userRepository.findByRoleType(RoleType.STUDENT)
            .shuffled()
            .take(2)

        val piece = pieceRepository.findAll().last()
        examService.save(user.map { it.id!! }.toList(), piece.id!!)
        // examService.save(listOf(1,4),piece.id!!)
    }

    @Test
    fun readAllProblems() {
        val response = pieceService.get(
            userId = 1L,
            pieceId = 1L
        )
        response.map {
            println(it.id)
            println(it.unitCode.name)
            println(it.level)
            println(it.problemType)
            println("-------------------------")
        }
    }

    @Test
    fun mark() {
        val user = userRepository.findByRoleType(RoleType.STUDENT).last()
        val piece = pieceRepository.findAll().last()
        val problemPiece = problemPieceRepository.findByPiece(piece)

        val requests = problemPiece.map {
            PieceMarkRequest(
                problemId = it.problem.id!!,
                answer = 1,
            )
        }
        val res = pieceService.mark(user.id!!, piece.id!!, requests)
        res.map {
            println(it.markResultType.name)
        }
    }
}