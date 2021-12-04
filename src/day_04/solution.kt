package day_04

import readInput
import readTestInput

const val BOARD_SIZE = 5

class Board(val id: Int, private val board: List<List<Int>>) {
    private val numberPositions = computeNumberPositions(board)
    private val hits = mutableSetOf<Position>()
    var isWon = false

    data class Position(val x: Int, val y: Int)

    private fun computeNumberPositions(boardConfig: List<List<Int>>): Map<Int, Position> {
        return boardConfig
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, num ->
                    Pair(num, Position(x, y))
                }
            }
            .toMap()
    }

    fun hit(num: Int) {
        val numPosition = numberPositions[num] ?: return

        hits.add(numPosition)

        updateBoardIsWon()
    }

    private fun updateBoardIsWon() {
        if (rowIsComplete() || columnIsComplete())
            isWon = true
    }

    private fun rowIsComplete(): Boolean {
        for (y in 0 until BOARD_SIZE) {
            var success = true

            for (x in 0 until BOARD_SIZE) {
                if (Position(x, y) !in hits)
                    success = false
            }

            if (success)
                return true
        }

        return false
    }

    private fun columnIsComplete(): Boolean {
        for (x in 0 until BOARD_SIZE) {
            var success = true

            for (y in 0 until BOARD_SIZE) {
                if (Position(x, y) !in hits)
                    success = false
            }

            if (success)
                return true
        }

        return false
    }

    fun getNoHitScore(): Int {
        return board.foldIndexed(0) { y, sum, row ->
            val rowSum = row.foldIndexed(0) { x, rowSum, num ->
                if (Position(x, y) !in hits)
                    rowSum + num
                else
                    rowSum
            }

            sum + rowSum
        }
    }
}

fun part1(input: List<String>): Int {
    val (numsCalled, boards) = getNumsCalledAndBoards(input)

    numsCalled.forEach { num ->
        boards.forEach { board ->
            board.hit(num)

            if (board.isWon) return board.getNoHitScore() * num
        }
    }

    return 0
}

private fun getNumsCalledAndBoards(input: List<String>): Pair<List<Int>, List<Board>> {
    val numsCalled = input
        .first()
        .split(",")
        .map { it.toInt() }

    val boards = input
        .drop(2)
        .joinToString("\n")
        .split("\n\n")
        .mapIndexed { index, board ->
            Board(
                index,
                board
                    .split("\n")
                    .map { line ->
                        line
                            .trim()
                            .split(Regex("\\s+"))
                            .map { it.toInt() }
                    }
            )
        }

    return Pair(numsCalled, boards)
}

fun part2(input: List<String>): Int {
    val (numsCalled, boards) = getNumsCalledAndBoards(input)
    val wonBoards = mutableSetOf<Int>()

    numsCalled.forEach { num ->
        boards.forEach { board ->
            if (!board.isWon) {
                board.hit(num)

                if (board.isWon) {
                    wonBoards.add(board.id)

                    if (wonBoards.size == boards.size) {
                        return board.getNoHitScore() * num
                    }
                }
            }
        }
    }

    return 0
}

fun main() {
    val input = readInput(4)
    val testInput = readTestInput(4)

    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    println(part1(input))
    println(part2(input))
}
