import java.io.File
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs

fun findShortest(field: List<String>, startPred: (Char) -> Boolean): Int {
    val n = field.size
    val m = field[0].length

    fun getHeight(x: Int, y: Int): Char {
        return if (x in 0 until n && y in 0 until m) {
            when (field[x][y]) {
                'S' -> 'a'
                'E' -> 'z'
                else -> field[x][y]
            }
        } else {
            'z' + 10
        }
    }

    val d = List(n) { MutableList(m) { Int.MAX_VALUE } }

    val q: Queue<Pair<Int, Int>> = LinkedList()
    for (i in 0 until n) {
        for (j in 0 until m) {
            if (startPred(field[i][j])) {
                q.add(i to j)
                d[i][j] = 0
            }
        }
    }
    while (q.isNotEmpty()) {
        val (x, y) = q.remove()
        val dist = d[x][y]
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (abs(dx) + abs(dy) != 1) {
                    continue
                }
                if (getHeight(x + dx, y + dy) <= getHeight(x, y) + 1 && d[x + dx][y + dy] > dist + 1) {
                    d[x + dx][y + dy] = dist + 1
                    q.add((x + dx) to (y + dy))
                }
            }
        }
    }

    val need = (0 until n).map { i ->
        field[i].mapIndexedNotNull { j, c -> if (c == 'E') j else null }
    }.withIndex().single { it.value.isNotEmpty() }.let { it.index to it.value.single() }

    return d[need.first][need.second]
}

fun solveEasy(lines: List<String>) {
    println(findShortest(lines) { it == 'S' })
}

fun solveHard(lines: List<String>) {
    println(findShortest(lines) { it in "Sa" })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}