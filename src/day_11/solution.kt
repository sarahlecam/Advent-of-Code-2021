package day_11

import readInput
import readTestInput

data class Position(val x: Int, val y: Int)

class Octopus(initialLoad: Int, x: Int, y: Int) {
    val pos = Position(x, y)

    var load = initialLoad
    var flashed = false

    fun reset() {
        load = 0
        flashed = false
    }
}

class Octopi(input: List<String>) {
    var octopi = extractOctopi(input)
    var flashes = 0

    fun step(iterations: Int) {
        for (i in 1..iterations) {
            step()
        }
    }

    private fun step() {
        increaseLoads()

        var stepFlashes = 0

        while (!allOctopiFlashed()) {
            stepFlashes += flashOctopi()
        }

        flashes += stepFlashes
        resetOctopi()
    }

    private fun allOctopiFlashed() = !octopi.any { !it.flashed && it.load > 9 }

    private fun increaseLoads() {
        octopi.forEach { octopus ->
            octopus.load++
        }
    }

    private fun flashOctopi(): Int {
        return octopi.foldIndexed(0) { ind, stepFlashes, octopus ->
            if (octopus.load > 9 && !octopus.flashed) {
                flashOctopus(octopus, ind)
                stepFlashes + 1
            } else
                stepFlashes
        }
    }

    private fun resetOctopi() {
        octopi.forEach { octopus ->
            if (octopus.flashed)
                octopus.reset()
        }
    }

    private fun flashOctopus(octopus: Octopus, ind: Int) {
        val (x, y) = octopus.pos

        if (x > 0) {
            octopi[ind - 1].load++

            if (y > 0) {
                octopi[ind - 11].load++
            }

            if (y < 9) {
                octopi[ind + 9].load++
            }
        }

        if (x < 9) {
            octopi[ind + 1].load++

            if (y > 0) {
                octopi[ind - 9].load++
            }

            if (y < 9) {
                octopi[ind + 11].load++
            }
        }

        if (y > 0) {
            octopi[ind - 10].load++
        }

        if (y < 9) {
            octopi[ind + 10].load++
        }

        octopus.flashed = true
    }

    private fun extractOctopi(input: List<String>) =
        input.mapIndexed { y, line -> line.mapIndexed { x, char -> Octopus(char.toString().toInt(), x, y) } }.flatten()
}

fun part1(input: List<String>): Int {
    val octopi = Octopi(input)

    octopi.step(100)

    return octopi.flashes
}

fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    val input = readInput(11)
    val testInput = readTestInput(11)

    check(part1(testInput) == 1656)
//    check(part2(testInput) == 1134)

    println(part1(input))
    println(part2(input))
}
