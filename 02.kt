import java.io.File

fun letterToNumber(s: String): Int? {
    check(s.length == 1)
    return when (val c = s[0]) {
        in "ABC" -> {
            c - 'A'
        }
        in "XYZ" -> {
            c - 'X'
        }
        else -> {
            null
        }
    }
}

fun solveEasy(lines: List<String>) {
    println(lines.map { line ->
        val (opponent, us) = line.split(' ').map { letterToNumber(it)!! }
        val outcome = (-1..1).singleOrNull { (opponent + it + 3) % 3 == us }!!
        1 + us + (outcome + 1) * 3
    }.sum())
}

fun solveHard(lines: List<String>) {
    println(lines.map { line ->
        val (opponent, outcome) = line.split(' ').map { letterToNumber(it)!! }
        val us = (opponent + outcome + 2) % 3
        1 + us + outcome * 3
    }.sum())
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}