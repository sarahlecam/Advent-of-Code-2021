package day_07

import readInput
import readTestInput
import kotlin.math.abs

fun part1(input: List<String>): Int {
    val positions = extractPositions(input)
    val score = { pos: Int, i: Int -> abs(pos - i) }

    return findBestFuelScore(positions, score)
}

fun part2(input: List<String>): Int {
    val positions = extractPositions(input)
    val computeFuelCost = { pos: Int, i: Int ->
        val dif = abs(pos - i)
        (dif * (dif + 1)) / 2
    }

    return findBestFuelScore(positions, computeFuelCost)
}

private fun extractPositions(input: List<String>): List<Int> = input.first().split(",").map { it.toInt() }

private fun findBestFuelScore(positions: List<Int>, computeFuelCost: (Int, Int) -> Int): Int {
    val max = positions.maxOrNull()!!
    val min = positions.minOrNull()!!

    var bestScore = Int.MAX_VALUE

    for (i in min..max) {
        val score = positions.fold(0) { score, position ->
            score + computeFuelCost(position, i)
        }

        if (score < bestScore) {
            bestScore = score
        }
    }

    return bestScore
}

fun main() {
    val input = readInput(7)
    val testInput = readTestInput(7)

    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    println(part1(input))
    println(part2(input))
}
