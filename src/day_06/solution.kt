package day_06

import readInput
import readTestInput

class Squad(initialFishes: List<Int>) {
    private var fishCounts = initialFishes
        .fold(mutableMapOf<Int, Long>()) { fishCounts, daysUntil ->
            fishCounts[daysUntil] = fishCounts.getOrDefault(daysUntil, 0) + 1
            fishCounts
        }

    fun advanceNDays(days: Int, fishCountsATM: MutableMap<Int, Long> = fishCounts) {
        if (days == 0) {
            fishCounts = fishCountsATM
            return
        }

        val nextDict = mutableMapOf<Int, Long>()

        nextDict[6] = fishCountsATM[0] ?: 0
        nextDict[8] = fishCountsATM[0] ?: 0

        for (daysUntil in 1..8) {
            nextDict[daysUntil - 1] = (nextDict[daysUntil - 1] ?: 0) + (fishCountsATM[daysUntil] ?: 0)
        }

        advanceNDays(days - 1, nextDict)
    }

    fun getNumFishes() = fishCounts.values.sum()
}

fun part1(input: List<String>): Long {
    val squad = Squad(getInitialFishes(input))

    squad.advanceNDays(80)

    return squad.getNumFishes()
}

fun part2(input: List<String>): Long {
    val squad = Squad(getInitialFishes(input))

    squad.advanceNDays(256)

    return squad.getNumFishes()
}

private fun getInitialFishes(input: List<String>): List<Int> {
    return input
        .first()
        .split(",")
        .map { it.toInt() }
}

fun main() {
    val input = readInput(6)
    val testInput = readTestInput(6)

    check(part1(testInput) == 5934.toLong())
    check(part2(testInput) == 26984457539)

    println(part1(input))
    println(part2(input))
}
