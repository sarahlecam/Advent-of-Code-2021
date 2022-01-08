package day_12

import readInput
import readTestInput

enum class CaveTye {
    START,
    END,
    SMALL,
    LARGE
}

class Cave(name: String, type: CaveTye) {
    companion object {
        fun make(name: String): Cave {
            return when (name) {
                "start" -> Cave(name, CaveTye.START)
                "end" -> Cave(name, CaveTye.END)
                name.lowercase() -> Cave(name, CaveTye.SMALL)
                else -> Cave(name, CaveTye.LARGE)
            }
        }
    }

    val adjacent = mutableListOf<Cave>()
}

fun part1(input: List<String>): Int {
    val caves = mutableMapOf<String, Cave>()

    input.forEach { connection ->
        val caveList = connection.split("-").map { caveName ->
            caves.getOrPut(caveName) { Cave.make(caveName) }
        }
        val connectedCaves = Pair(caveList.first(), caveList.last())

        connectedCaves.first.adjacent.add(connectedCaves.second)
        connectedCaves.second.adjacent.add(connectedCaves.first)
    }

    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    val input = readInput(12)
    val testInput = readTestInput(12)

    check(part1(testInput) == 19)
//    check(part2(testInput) == 1134)

    println(part1(input))
    println(part2(input))
}
