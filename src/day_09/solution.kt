package day_09

import readInput
import readTestInput

fun part1(input: List<String>): Int {
    val heightMap = parseArray(input)
    val lowestPoints = findLowestPoints(heightMap)

    return lowestPoints.fold(0) { acc, (x, y) -> acc + heightMap[y][x] + 1 }
}

private fun findLowestPoints(heightMap: List<List<Int>>): MutableList<Pair<Int, Int>> {
    val lowestPoints = mutableListOf<Pair<Int, Int>>()

    for (y in heightMap.indices) {
        for (x in 0 until heightMap.first().size) {
            val height = heightMap[y][x]

            val up = if (y > 0) heightMap[y - 1][x] else Int.MAX_VALUE
            val left = if (x > 0) heightMap[y][x - 1] else Int.MAX_VALUE
            val down = if (y < heightMap.size - 1) heightMap[y + 1][x] else Int.MAX_VALUE
            val right = if (x < heightMap.first().size - 1) heightMap[y][x + 1] else Int.MAX_VALUE

            if (height < up && height < left && height < down && height < right) {
                lowestPoints.add(Pair(x, y))
            }
        }
    }

    return lowestPoints
}

fun part2(input: List<String>): Int {
    val heightMap = parseArray(input)
    val lowestPoints = findLowestPoints(heightMap)
    val basinSizes = lowestPoints.map { coords -> findBasinSize(coords, heightMap) }

    return basinSizes.sortedDescending().take(3).reduce { acc, i -> acc * i }
}

private fun parseArray(input: List<String>) =
    input.map { it.toCharArray().map { it.toString().toInt() } }

fun findBasinSize(coords: Pair<Int, Int>, heightMap: List<List<Int>>, visited: MutableList<Pair<Int, Int>> = mutableListOf()): Int {
    val (x, y) = coords

    if (heightMap[y][x] >= 9 || visited.contains(coords))
        return 0

    visited.add(coords)

    return (
        1 +
            (if (y > 0) findBasinSize(Pair(x, y - 1,), heightMap, visited) else 0) +
            (if (x > 0) findBasinSize(Pair(x - 1, y), heightMap, visited) else 0) +
            (if (y < heightMap.size - 1) findBasinSize(Pair(x, y + 1), heightMap, visited) else 0) +
            (if (x < heightMap.first().size - 1) findBasinSize(Pair(x + 1, y), heightMap, visited) else 0)
        )
}

fun main() {
    val input = readInput(9)
    val testInput = readTestInput(9)

    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    println(part1(input))
    println(part2(input))
}
