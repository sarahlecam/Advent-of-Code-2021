package day_08

import readInput
import readTestInput

val NUMBER_MAPPINGS = mutableMapOf<Int, Set<Char>>(
    0 to setOf('a', 'b', 'c', 'e', 'f', 'g'),
    1 to setOf('c', 'f'),
    2 to setOf('a', 'c', 'd', 'e', 'g'),
    3 to setOf('a', 'c', 'd', 'f', 'g'),
    4 to setOf('b', 'c', 'd', 'f'),
    5 to setOf('a', 'b', 'd', 'f', 'g'),
    6 to setOf('a', 'b', 'd', 'e', 'f', 'g'),
    7 to setOf('a', 'c', 'f'),
    8 to setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
    9 to setOf('a', 'b', 'c', 'd', 'f', 'g'),
)
val obviNumbers = listOf(1, 4, 7, 8).associateBy { NUMBER_MAPPINGS[it]!!.size }

fun part1(input: List<String>): Int {
    val parsedInput = parseInput(input)

    return parsedInput.fold(0) { num, line ->
        val output = line.last()
        num + output.count { it.length in obviNumbers.keys }
    }
}

fun parseInput(input: List<String>): List<List<List<String>>> {
    return input.map { line ->
        line
            .split(" | ")
            .map { it.split(" ") }
    }
}

fun part2(input: List<String>): Int {
    val parsedInput = parseInput(input)

    return parsedInput.fold(0) { sum, line ->
        val output = line.last()
        val charMapping = findCharMapping(line)

        sum + computeOutputValue(output, charMapping)
    }
}

private fun findCharMapping(input: List<List<String>>): MutableMap<Char, Char> {
    val allNumbers = input.flatten()

    val simpleNumberMappings = getSimpleNumberMapping(allNumbers)
    val nonSimpleNumbers = allNumbers.filter { it !in simpleNumberMappings.values.map { it.joinToString() } }

    val (charMapping, pairMapping) = findInitialClues(simpleNumberMappings)

    completeMapping(nonSimpleNumbers, charMapping, pairMapping)

    return charMapping
}

private fun completeMapping(
    nonSimpleNumbers: List<String>,
    charMapping: MutableMap<Char, Char>,
    pairMapping: MutableMap<Pair<Char, Char>, Pair<Char, Char>>
) {
    nonSimpleNumbers.forEach { numberString ->
        val filterActual = filterActualLetters(numberString.toSet(), charMapping, pairMapping)

        if (canPotentiallyDetermineMatch(filterActual)) {
            val potentialNumberStringMatches = NUMBER_MAPPINGS.values.filter {
                it.size == numberString.length &&
                    it.containsAll(
                        numberString.toSet().filter { it in charMapping.keys }.map { charMapping[it]!! }
                    )
            }
            val filteredExpectedMatches =
                potentialNumberStringMatches.flatMap { filterExpectedLetters(it, charMapping, pairMapping) }

            filterActual.forEach { unmatchedLetter ->
                val pair = pairMapping.keys.find { it.first == unmatchedLetter || it.second == unmatchedLetter }!!
                val other = if (unmatchedLetter == pair.first) pair.second else pair.first
                val expectedPair = pairMapping[pair]!!

                if (canDetermineMatch(filteredExpectedMatches, expectedPair)) {
                    val match = filteredExpectedMatches.find { it == expectedPair.first || it == expectedPair.second }!!
                    val expectedOther = if (match == expectedPair.first) expectedPair.second else expectedPair.first

                    charMapping[unmatchedLetter] = match
                    charMapping[other] = expectedOther

                    pairMapping.remove(pair)
                }
            }
        }
    }
}

private fun canPotentiallyDetermineMatch(filterActual: List<Char>) =
    filterActual.isNotEmpty()

private fun canDetermineMatch(
    filteredExpected: List<Char>,
    expectedPair: Pair<Char, Char>
) = !filteredExpected.containsAll(expectedPair.toList())

fun getSimpleNumberMapping(allNumbers: List<String>): MutableMap<Int, Set<Char>> {
    val numberMap = allNumbers.fold(mutableMapOf<Int, Set<Char>>()) { numberMapping, numberString ->
        if (numberString.length in obviNumbers.keys) {
            val number = obviNumbers[numberString.length]!!
            numberMapping[number] = numberString.toSet()
        }
        numberMapping
    }

    check(obviNumbers.values.all { numberMap.containsKey(it) })

    return numberMap
}

private fun findInitialClues(numberMappings: Map<Int, Set<Char>>): Pair<MutableMap<Char, Char>, MutableMap<Pair<Char, Char>, Pair<Char, Char>>> {
    val charMapping = mutableMapOf<Char, Char>()
    val fuzzyMapping = mutableMapOf<Pair<Char, Char>, Pair<Char, Char>>()

    addMatch(numberMappings[1]!!, NUMBER_MAPPINGS[1]!!, charMapping, fuzzyMapping)
    addMatch(numberMappings[7]!!, NUMBER_MAPPINGS[7]!!, charMapping, fuzzyMapping)
    addMatch(numberMappings[4]!!, NUMBER_MAPPINGS[4]!!, charMapping, fuzzyMapping)
    addMatch(numberMappings[8]!!, NUMBER_MAPPINGS[8]!!, charMapping, fuzzyMapping)

    return charMapping to fuzzyMapping
}

private fun addMatch(
    actualLetterSet: Set<Char>,
    expectedLetterSet: Set<Char>,
    charMapping: MutableMap<Char, Char>,
    fuzzyMapping: MutableMap<Pair<Char, Char>, Pair<Char, Char>>,
) {
    val filteredActualLetterSet = filterActualLetters(actualLetterSet, charMapping, fuzzyMapping)
    val filteredExpectedLetterSet = filterExpectedLetters(expectedLetterSet, charMapping, fuzzyMapping)

    check(filteredActualLetterSet.size == 1 || filteredActualLetterSet.size == 2)
    check(filteredExpectedLetterSet.size == 1 || filteredExpectedLetterSet.size == 2)

    if (filteredActualLetterSet.size == 1) {
        charMapping[filteredActualLetterSet.first()] = filteredExpectedLetterSet.first()
    } else {
        fuzzyMapping[Pair(filteredActualLetterSet.first(), filteredActualLetterSet.last())] = Pair(
            filteredExpectedLetterSet.first(),
            filteredExpectedLetterSet.last()
        )
    }
}

private fun filterActualLetters(
    actualLetterSet: Set<Char>,
    realMappings: Map<Char, Char>,
    fuzzyMappings: Map<Pair<Char, Char>, Pair<Char, Char>>
): List<Char> {
    val letterSetWOPairs = removePairs(actualLetterSet, fuzzyMappings.keys)

    return letterSetWOPairs.filterNot { it in realMappings.keys }
}

private fun removePairs(
    actualLetterSet: Set<Char>,
    pairSetToRemove: Collection<Pair<Char, Char>>
): Set<Char> {
    val mutableLetterSet = actualLetterSet.toMutableSet()

    pairSetToRemove.forEach { (firstChar, secondChar) ->
        if (actualLetterSet.contains(firstChar) && actualLetterSet.contains(secondChar)) {
            mutableLetterSet.remove(firstChar)
            mutableLetterSet.remove(secondChar)
        }
    }

    return mutableLetterSet
}

private fun filterExpectedLetters(
    expectedLetterSet: Set<Char>,
    realMappings: Map<Char, Char>,
    fuzzyMappings: Map<Pair<Char, Char>, Pair<Char, Char>>
): List<Char> {
    val letterSetWOPairs = removePairs(expectedLetterSet, fuzzyMappings.values)

    return letterSetWOPairs.filterNot { it in realMappings.values }
}

fun computeOutputValue(
    output: List<String>,
    defMappings: Map<Char, Char>
) = output.map { numberString ->
    numberString
        .map { defMappings[it]!! }
}.map { expectedLetters ->
    NUMBER_MAPPINGS.entries.find { it.value.size == expectedLetters.size && it.value.containsAll(expectedLetters) }!!.key
}.joinToString("").toInt()

fun main() {
    val input = readInput(8)
    val testInput = readTestInput(8)

    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    println(part1(input))
    println(part2(input))
}
