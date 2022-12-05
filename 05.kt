import java.io.File

fun solveEasy(lines: List<String>) {
    val n = lines[0].length / 4 + 1
    val a = List(n) { mutableListOf<Char>() }
    lines.split("")[0].forEach { s ->
        s.indices.filter { s[it] == '[' }.forEach { i ->
            a[i / 4].add(s[i + 1])
        }
    }
    a.forEach { it.reverse() }
    lines.filter { it.startsWith("move") }.forEach { instruction ->
        val (count, from, to) = instruction.split(" ").slice(1..5 step 2).map(String::toInt)
        val chunk = a[from - 1].takeLast(count).reversed()
        repeat(count) {
            a[from - 1].removeLast()
        }
        a[to - 1].addAll(chunk)
    }
    println(a.map { it.last() }.joinToString(""))
}

fun solveHard(lines: List<String>) {
    val n = lines[0].length / 4 + 1
    val a = List(n) { mutableListOf<Char>() }
    lines.split("")[0].forEach { s ->
        s.indices.filter { s[it] == '[' }.forEach { i ->
            a[i / 4].add(s[i + 1])
        }
    }
    a.forEach { it.reverse() }
    lines.filter { it.startsWith("move") }.forEach { instruction ->
        val (count, from, to) = instruction.split(" ").slice(1..5 step 2).map(String::toInt)
        val chunk = a[from - 1].takeLast(count)
        repeat(count) {
            a[from - 1].removeLast()
        }
        a[to - 1].addAll(chunk)
    }
    println(a.map { it.last() }.joinToString(""))
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}