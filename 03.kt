import java.io.File

fun charPriority(c: Char): Int = when (c) {
    in 'a'..'z' -> c - 'a' + 1
    in 'A'..'Z' -> c - 'A' + 27
    else -> error("(")
}

fun String.toPrioritySet() = map { charPriority(it) }.toSet()

fun solveEasy(lines: List<String>) {
    println(lines.map {
        val n = it.length
        (it.substring(0, n / 2).toPrioritySet() intersect it.substring(n / 2).toPrioritySet()).singleOrNull()!!
    }.sum())
}

fun solveHard(lines: List<String>) {
    println(lines.chunked(3).map {
        it.map(String::toPrioritySet).reduce(Set<Int>::intersect).singleOrNull()!!
    }.sum())
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}