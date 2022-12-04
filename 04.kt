import java.io.File
import kotlin.math.*

fun String.toRange(): IntRange {
    val arg = split('-').map(String::toInt)
    return IntRange(arg[0], arg[1])
}

infix fun IntRange.contains(r: IntRange): Boolean = contains(r.first) && contains(r.last)
infix fun IntRange.intersects(r: IntRange): Boolean = max(first, r.first) <= min(last, r.last)

fun solveEasy(lines: List<String>) {
    println(lines.count {
        val (rLeft, rRight) = it.split(',').map(String::toRange)
        rLeft contains rRight || rRight contains rLeft
    })
}

fun solveHard(lines: List<String>) {
    println(lines.count {
        val (rLeft, rRight) = it.split(',').map(String::toRange)
        rLeft intersects rRight
    })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}