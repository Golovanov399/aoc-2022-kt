import java.io.File
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

operator fun Point.plus(b: Point) = Point(x + b.x, y + b.y)
operator fun Point.minus(b: Point) = Point(x - b.x, y - b.y)

fun sign(x: Int) = when {
    x < 0 -> -1
    x > 0 -> 1
    else -> 0
}

fun adjust(what: Point, accordingToWhat: Point): Point {
    val dif = accordingToWhat - what
    return if (abs(dif.x) <= 1 && abs(dif.y) <= 1) {
        what
    } else {
        what + Point(sign(dif.x), sign(dif.y))
    }
}

fun solveEasy(lines: List<String>) {
    var head = Point(0, 0)
    var tail = Point(0, 0)
    val visited = mutableSetOf(tail)
    for ((dir, steps) in lines.map { it.substringBefore(" ") to it.substringAfter(" ").toInt() }) {
        val shift = when (dir) {
            "L" -> Point(-1, 0)
            "R" -> Point(1, 0)
            "U" -> Point(0, 1)
            "D" -> Point(0, -1)
            else -> error("(")
        }
        repeat (steps) {
            head += shift
            tail = adjust(tail, head)
            visited.add(tail)
        }
    }
    println(visited.size)
}

fun solveHard(lines: List<String>) {
    val rope = MutableList(10) { Point(0, 0) }
    val visited = mutableSetOf(rope[0])
    for ((dir, steps) in lines.map { it.substringBefore(" ") to it.substringAfter(" ").toInt() }) {
        val shift = when (dir) {
            "L" -> Point(-1, 0)
            "R" -> Point(1, 0)
            "U" -> Point(0, 1)
            "D" -> Point(0, -1)
            else -> error("(")
        }
        repeat (steps) {
            rope[0] += shift
            for (i in 1 until rope.size) {
                rope[i] = adjust(rope[i], rope[i - 1])
            }
            visited.add(rope.last())
        }
    }
    println(visited.size)
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}