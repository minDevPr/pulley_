package com.api.pulley.internal

import java.math.RoundingMode
import kotlin.math.roundToLong

enum class RoleType{
    STUDENT, TEACHER
}

enum class ProblemType{
    SUBJECTIVE, SELECTION, ALL
}

enum class MarkResultType{
    PASS, FAIL
}

enum class LevelType(
    private val lowRate: Double,
    private val middleRate: Double,
    private val highRate: Double
) {
    HIGH(0.2, 0.3, 0.5),
    MIDDLE(0.25, 0.5, 0.25),
    LOW(0.5, 0.3, 0.2);

    fun toRate(totalCount: Int): Triple<Pair<IntRange, Long>, Pair<IntRange, Long>, Pair<IntRange, Long>> {
        var lowCount = (totalCount * lowRate).roundToLong()
        var middleCount = (totalCount * middleRate).roundToLong()
        var highCount = (totalCount * highRate).roundToLong()

        val sum = lowCount + middleCount + highCount
        when {
            sum < totalCount -> middleCount += (totalCount - sum)
            sum > totalCount -> {
                when (maxOf(lowCount, middleCount, highCount)) {
                    lowCount -> lowCount -= (sum - totalCount)
                    middleCount -> middleCount -= (sum - totalCount)
                    else -> highCount -= (sum - totalCount)
                }
            }
        }

        return Triple(
            Utils.LevelRanges.LOW_RANGE to lowCount,
            Utils.LevelRanges.MIDDLE_RANGE to middleCount,
            Utils.LevelRanges.HIGH_RANGE to highCount
        )
    }
}