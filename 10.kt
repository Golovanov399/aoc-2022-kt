import java.io.File
import kotlin.math.abs

fun solveEasy(lines: List<String>) {
    var sum = 0
    var current = 1
    var cycle = 0
    fun increaseCycle() {
        cycle += 1
        if (cycle % 40 == 20) {
            sum += cycle * current
        }
    }
    lines.forEach {
        when (it.substringBefore(' ')) {
            "noop" -> increaseCycle()
            "addx" -> {
                repeat(2) {
                    increaseCycle()
                }
                current += it.substringAfter(' ').toInt()
            }
            else -> error("(")
        }
    }
    println(sum)
}

fun solveHard(lines: List<String>) {
    var current = 1
    val output = mutableListOf<Char>()
    fun increaseCycle() {
        val len = output.size % 40
        output.add(if (abs(len - current) <= 1) '#' else '.')
    }
    lines.forEach {
        when (it.substringBefore(' ')) {
            "noop" -> increaseCycle()
            "addx" -> {
                repeat(2) {
                    increaseCycle()
                }
                current += it.substringAfter(' ').toInt()
            }
            else -> error("(")
        }
    }
    output.chunked(40).forEach { println(it.joinToString("")) }
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}