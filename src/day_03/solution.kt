package day_03

import readInput
import readTestInput

const val ZERO = '0'
const val ONE = '1'

fun part1(input: List<String>): Int {
    val gamma = getGammaBinaryString(input)
    val epsilon = inverseBinary(gamma)

    return multiplyBinaryToInt(gamma, epsilon)
}

private fun getGammaBinaryString(binaryList: List<String>): String {
    var gamma = ""
    val binaryStringLength = binaryList.first().length

    for (i in 0 until binaryStringLength) {
        gamma += getMostPopBit(binaryList, i)
    }

    return gamma
}

private fun getMostPopBit(binaryList: List<String>, position: Int): Char {
    val counts = binaryList.fold(
        mutableMapOf(
            ZERO to 0,
            ONE to 0,
        )
    ) { counts, binaryString ->
        val bit = binaryString[position]
        counts[bit] = counts[bit]!! + 1

        counts
    }

    return if (counts[ZERO]!! > counts[ONE]!!) ZERO else ONE
}

private fun inverseBinary(binaryString: String) =
    binaryString
        .map { if (it == ZERO) ONE else ZERO }
        .joinToString("")

private fun multiplyBinaryToInt(binary1: String, binary2: String) =
    Integer.parseInt(binary1, 2) * Integer.parseInt(binary2, 2)

fun part2(input: List<String>): Int {
    val binaryStringLength = input.first().length

    var oxygenRatings = input
    var co2Ratings = input

    for (i in 0 until binaryStringLength) {
        val oxyMostPopBit = getMostPopBit(oxygenRatings, i)
        val co2leastPopBit = getLeastPopBit(co2Ratings, i)

        if (oxygenRatings.size > 1)
            oxygenRatings = oxygenRatings.filter { it[i] == oxyMostPopBit }

        if (co2Ratings.size > 1)
            co2Ratings = co2Ratings.filter { it[i] == co2leastPopBit }

        if (oxygenRatings.size == 1 && co2Ratings.size == 1)
            break
    }

    check(oxygenRatings.size == 1)
    check(co2Ratings.size == 1)

    return multiplyBinaryToInt(oxygenRatings.first(), co2Ratings.first())
}

private fun getLeastPopBit(binaryList: List<String>, position: Int) =
    if (getMostPopBit(binaryList, position) == ZERO) ONE else ZERO

fun main() {
    val input = readInput(3)
    val testInput = readTestInput(3)

    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    println(part1(input))
    println(part2(input))
}
