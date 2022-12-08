import java.io.File

fun solveEasy(lines: List<String>) {
    val n = lines.size
    val m = lines[0].length
    val def = '0' - 1
    println((0 until n).sumOf { i ->
        (0 until m).filter { j ->
            val c = lines[i][j]
            (c > (0 until i).maxOfOrNull { lines[it][j] } ?: def)
                    || (c > (i + 1 until n).maxOfOrNull { lines[it][j] } ?: def)
                    || (c > (0 until j).maxOfOrNull { lines[i][it] } ?: def)
                    || (c > (j + 1 until m).maxOfOrNull { lines[i][it] } ?: def)
        }.size
    })
}

fun solveHard(lines: List<String>) {
    val n = lines.size
    val m = lines[0].length
    println((1 until n - 1).maxOf { i ->
        (1 until m - 1).maxOf { j ->
            val c = lines[i][j]
            (i - ((0 until i).lastOrNull { lines[it][j] >= c } ?: 0)) *
                    (-i + ((i + 1 until n).firstOrNull { lines[it][j] >= c } ?: n - 1)) *
                    (j - ((0 until j).lastOrNull { lines[i][it] >= c } ?: 0)) *
                    (-j + ((j + 1 until m).firstOrNull { lines[i][it] >= c } ?: m - 1))
        }
    })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}