package day_01

import readInput
import readTestInput

fun part1(input: List<String>): Int {
    return countIncreases(input.map { it.toInt() })
}

fun part2(input: List<String>): Int {
    val windowedSums = input
        .map { it.toInt() }
        .windowed(3)
        .map { it.sum() }

    return countIncreases(windowedSums)
}

private fun countIncreases(nums: List<Int>): Int {
    return nums
        .windowed(2)
        .count { (first, second) -> first < second }
}

fun main() {
    val input = readInput(1)
    val testInput = readTestInput(1)

    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    println(part1(input))
    println(part2(input))
}
