import java.io.File
import kotlin.math.max

fun solveEasy(lines: List<String>) {
    val len = 4
    println(lines[0].indices.first { i -> lines[0].substring(max(i - len, 0), i).toSet().size == len })
}

fun solveHard(lines: List<String>) {
    val len = 14
    println(lines[0].indices.first { i -> lines[0].substring(max(i - len, 0), i).toSet().size == len })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}