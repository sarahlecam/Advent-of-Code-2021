import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int): List<String> {
    return File(
        "src/${getDayStringRepresentation(day)}",
        "input.txt",
    ).readLines()
}

/**
 * Reads lines from the given test input txt file.
 */
fun readTestInput(day: Int): List<String> {
    return File(
        "src/${getDayStringRepresentation(day)}",
        "test_input.txt",
    ).readLines()
}

private fun getDayStringRepresentation(day: Int): String {
    val dayString = day.toString()
    return "day_" +
        if (dayString.length > 1) dayString else "0$dayString"
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
