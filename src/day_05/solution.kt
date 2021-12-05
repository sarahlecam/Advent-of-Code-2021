package day_05

import readInput
import readTestInput
import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    override fun toString() = "($x,$y)"
}

sealed class Line(open val start: Point, open val end: Point) {
    companion object {
        fun createLine(start: Point, end: Point): Line {
            return if (start.x == end.x)
                VerticalLine(start, end)
            else if (start.y == end.y)
                HorizontalLine(start, end)
            else
                DiagonalLine(start, end)
        }
    }

    abstract fun findPoints(): Set<Point>
    override fun toString() = "(${start.x},${start.y}) - (${end.x},${end.y})"

    class VerticalLine(override val start: Point, override val end: Point) : Line(start, end) {
        override fun findPoints(): Set<Point> {
            val points = mutableSetOf<Point>()

            for (y in start.y toward end.y) {
                points.add(Point(start.x, y))
            }

            return points
        }
    }

    class HorizontalLine(override val start: Point, override val end: Point) : Line(start, end) {
        override fun findPoints(): Set<Point> {
            val points = mutableSetOf<Point>()

            for (x in start.x toward end.x) {
                points.add(Point(x, start.y))
            }

            return points
        }
    }

    class DiagonalLine(override val start: Point, override val end: Point) : Line(start, end) {
        override fun findPoints(): Set<Point> {
            val points = mutableSetOf<Point>()

            val xDir = if (start.x < end.x) 1 else -1
            val yDir = if (start.y < end.y) 1 else -1
            val difference = abs(end.x - start.x)

            for (i in 0..difference) {
                val x = if (xDir == 1) start.x + i else start.x - i
                val y = if (yDir == 1) start.y + i else start.y - i
                points.add(Point(x, y))
            }

            return points
        }
    }
}

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun part1(input: List<String>): Int {
    val lines = getLines(input)
    val hits = mutableMapOf<Point, Int>()

    lines.forEach { line ->
        if (line !is Line.DiagonalLine) {
            line.findPoints().forEach { point ->
                hits[point] = (hits[point] ?: 0) + 1
            }
        }
    }

    return hits.values.count { it > 1 }
}

private fun getLines(input: List<String>): List<Line> {
    return input
        .map { line ->
            val lineLimits = line
                .split(" -> ")
                .map { points ->
                    val pointCords = points
                        .split(",")
                        .map { it.toInt() }

                    Point(pointCords.first(), pointCords.last())
                }

            Line.createLine(lineLimits.first(), lineLimits.last())
        }
}

fun part2(input: List<String>): Int {
    val lines = getLines(input)
    val hits = mutableMapOf<Point, Int>()

    lines.forEach { line ->
        line.findPoints().forEach { point ->
            hits[point] = (hits[point] ?: 0) + 1
        }
    }

    return hits.values.count { it > 1 }
}

fun main() {
    val input = readInput(5)
    val testInput = readTestInput(5)

    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    println(part1(input))
    println(part2(input))
}
