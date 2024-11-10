package com.api.pulley

import com.api.pulley.domain.piece.problemPiece.repository.ProblemPieceRepository
import com.api.pulley.domain.piece.repository.PieceRepository
import com.api.pulley.domain.problem.repository.ProblemRepository
import com.api.pulley.domain.unitCode.repository.UnitCodeRepository
import com.api.pulley.domain.user.repository.UserRepository
import com.api.pulley.internal.LevelType
import com.api.pulley.internal.ProblemType
import com.api.pulley.internal.RoleType
import com.api.pulley.internal.Utils
import com.api.pulley.service.UserPieceService
import com.api.pulley.service.PieceService
import com.api.pulley.service.ProblemService
import com.api.pulley.web.dto.request.PieceCreateRequest
import com.api.pulley.web.dto.request.PieceMarkRequest
import org.hibernate.boot.jaxb.SourceType
import org.junit.jupiter.api.Assertions
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
    fun problem_get_byFilter() {
        val unitCodes = unitCodeRepository.findAll()
            .map { it.unitCode }

        val totalCount = 10
        val levelType = LevelType.entries.random()
        val problemType = ProblemType.entries.random()

        val result = problemService.readAll(
            totalCount = totalCount,
            unitCodeList = unitCodes,
            levelType = levelType,
            problemType = problemType
        )

        // LevelType 에 따른 비율 검증
        val (low, middle, high) = levelType.toRate(totalCount)

        Assertions.assertEquals(low.second, result.count { it.level == 1 }.toLong())
        Assertions.assertEquals(middle.second, result.count { it.level in 2..4 }.toLong())
        Assertions.assertEquals(high.second, result.count { it.level == 5 }.toLong())

        // problemType 검증
        if (problemType != ProblemType.ALL) {
            result.forEach { problem ->
                Assertions.assertEquals(problemType, problem.problemType)
            }
        }
    }

    @Test
    fun save_piece() {
        val user = userRepository.findByRoleType(RoleType.TEACHER).last()
        val problems = problemRepository.findAll()
            .shuffled()
            .take(50)

        val request = PieceCreateRequest(
            problemIds = problems.map { it.id!! }.toSet(),
            name = "수학2"
        )
        val result = pieceService.save(user.id!!, request = request)

        Assertions.assertEquals(request.name, result.piece.name)
        Assertions.assertEquals(user.name, result.piece.user.name )
        Assertions.assertTrue(result.problems.map { it.id }.containsAll(request.problemIds))
    }

    @Test
    fun exam_to_user(){
        val user = userRepository.findByRoleType(RoleType.STUDENT)
            .shuffled()
            .take(4)

        val userIds = user.map { it.id!! }.toList()
        val piece = pieceRepository.findAll().last()
        val result = examService.save(userIds, piece.id!!)

        Assertions.assertTrue(result.users.map { it.id }.containsAll(userIds))
    }


    @Test
    fun read_problem_of_piece() {
        // val user = userRepository.findByRoleType(RoleType.STUDENT).last()
        val piece = pieceRepository.findById(1L).orElseThrow()

        val result = pieceService.read(5L, 1L)
        result?.map {
            println("id : " + it.id)
            println("unitcode : " + it.unitCode.name)
            println("level : " + it.level)
        }
    }

    @Test
    fun mark() {
        // val user = userRepository.findByRoleType(RoleType.STUDENT).last()
        val piece = pieceRepository.findById(1L).orElseThrow()
        val problemPiece = problemPieceRepository.findByPiece(piece)

        val requests = problemPiece.map {
            PieceMarkRequest(
                problemId = it.problem.id!!,
                answer = (1..5).random(),
            )
        }
        val result = pieceService.mark(2L, 1L, requests)
    }

    @Test
    fun anlayze() {
        val start = System.currentTimeMillis()
        val res = pieceService.analyze(1L)
        val end = System.currentTimeMillis()
        println("ms : " + end.minus(start))
    }
}