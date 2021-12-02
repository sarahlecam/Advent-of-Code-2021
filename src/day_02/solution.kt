package day_02

import readInput
import readTestInput

class ShipPosition {
    var depth = 0
    var horizontal = 0
    private var aim = 0

    fun getMoveScore() = depth * horizontal

    fun moveBasic(direction: String, amount: Int) {
        when (direction) {
            "up" -> depth -= amount
            "down" -> depth += amount
            "forward" -> horizontal += amount
        }
    }

    fun moveWAim(direction: String, amount: Int) {
        when (direction) {
            "up" -> aim -= amount
            "down" -> aim += amount
            "forward" -> {
                horizontal += amount
                depth += aim * amount
            }
        }
    }
}

fun part1(input: List<String>): Int {
    val shipPosition = ShipPosition()

    input
        .map { it.split(" ") }
        .forEach { shipPosition.moveBasic(it.first(), it.last().toInt()) }

    return shipPosition.getMoveScore()
}

fun part2(input: List<String>): Int {
    val shipPosition = ShipPosition()

    input
        .map { it.split(" ") }
        .forEach { shipPosition.moveWAim(it.first(), it.last().toInt()) }

    return shipPosition.getMoveScore()
}

fun main() {
    val input = readInput(2)
    val testInput = readTestInput(2)

    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    println(part1(input))
    println(part2(input))
}
