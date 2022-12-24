import java.io.File
import kotlin.math.abs

fun solveEasy(lines: List<String>) {
    val cubes = lines.map { it.split(',').map(String::toInt) }.toSet()
    val dirs = (-1..1).map { x ->
        (-1..1).map { y ->
            (-1..1).filter { z -> abs(x) + abs(y) + abs(z) == 1 }.map { z -> listOf(x, y, z) }
        }.flatten()
    }.flatten()
    println(cubes.sumOf { cube ->
        dirs.count { dir ->
            val cell = cube.zip(dir).map { (i, di) -> i + di }
            cell !in cubes
        }
    })
}

@ExperimentalStdlibApi
fun solveHard(lines: List<String>) {
    val cubes = lines.map { it.split(',').map(String::toInt) }.toSet()
    val dirs = (-1..1).map { x ->
        (-1..1).map { y ->
            (-1..1).filter { z -> abs(x) + abs(y) + abs(z) == 1 }.map { z -> listOf(x, y, z) }
        }.flatten()
    }.flatten()

    val ranges = (0..2).map { i ->
        val minValue = cubes.minOf { it[i] }
        val maxValue = cubes.maxOf { it[i] }
        (minValue-1)..(maxValue+1)
    }
    val sizes = ranges.map { it.count() }
    val used = List(sizes[0]) { List(sizes[1]) { MutableList(sizes[2]) { false } } }

    val dfs = DeepRecursiveFunction<List<Int>, Int> { cell ->
        if (cell.zip(ranges).any { (x, r) -> x !in r }) {
            return@DeepRecursiveFunction 0
        }
        val lookup = cell.zip(ranges).map { (x, r) -> x - r.first }
        if (used[lookup[0]][lookup[1]][lookup[2]]) {
            return@DeepRecursiveFunction 0
        }
        if (cell in cubes) {
            return@DeepRecursiveFunction 1
        }
        used[lookup[0]][lookup[1]][lookup[2]] = true
        return@DeepRecursiveFunction dirs.sumOf { dir ->
            callRecursive(cell.zip(dir).map { (a, da) -> a + da })
        }
    }

    println(dfs(ranges.map { it.first }))
}

@ExperimentalStdlibApi
fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}