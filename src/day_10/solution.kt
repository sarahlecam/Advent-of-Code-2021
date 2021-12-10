package day_10

import readInput
import readTestInput

val MATCHES = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<',
)

val PART1_SCORE = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)
val PART2_SCORE = mapOf(
    '(' to 1,
    '[' to 2,
    '{' to 3,
    '<' to 4,
)

fun part1(input: List<String>): Int {
    val score = input.fold(0) { score, line ->
        var lineScore = 0
        val stack = mutableListOf<Char>()

        for (char in line) {
            if (char in MATCHES.keys) {
                if (stack.isEmpty() || stack.last() != MATCHES[char]) {
                    lineScore = PART1_SCORE[char]!!
                    break
                } else {
                    stack.removeAt(stack.lastIndex)
                }
            } else {
                stack.add(char)
            }
        }

        score + lineScore
    }

    return score
}

fun part2(input: List<String>): Long {
    val scores = input.fold(listOf<Long>()) { scoreList, line ->
        val stack = mutableListOf<Char>()
        var isCorrupt = false

        for (char in line) {
            if (char in MATCHES.keys) {
                if (stack.isEmpty() || stack.last() != MATCHES[char]) {
                    isCorrupt = true
                    break
                } else
                    stack.removeAt(stack.lastIndex)
            } else
                stack.add(char)
        }

        if (isIncomplete(stack, isCorrupt)) {
            val score = stack.foldRight(0L) { char, lineScore ->
                lineScore * 5 + PART2_SCORE.getOrDefault(char, 0)
            }

            scoreList + score
        } else
            scoreList
    }

    return getMedian(scores)
}

private fun isIncomplete(stack: List<Char>, isCorrupt: Boolean) =
    stack.isNotEmpty() && !isCorrupt

private fun getMedian(scores: List<Long>): Long {
    val sortedScore = scores.sorted()
    val score = if (sortedScore.size % 2 != 0) {
        sortedScore[sortedScore.size / 2]
    } else {
        (sortedScore[sortedScore.size / 2] + sortedScore[sortedScore.size / 2 - 1]) / 2
    }
    return score
}

fun main() {
    val input = readInput(10)
    val testInput = readTestInput(10)

    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    println(part1(input))
    println(part2(input))
}
