import java.io.File

fun solveEasy(lines: List<String>) {
    val a = lines.split("")
    println(a.map { it.sumOf(String::toInt) }.maxOrNull()!!)
}

fun solveHard(lines: List<String>) {
    val a = lines.split("")
    println(a.map { it.sumOf(String::toInt) }.sorted().takeLast(3).sum())
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}