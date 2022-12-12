import java.io.File

data class Monkey(
    val items: MutableList<Int>,
    val operation: String,
    val test: Int,
    val to: List<Int>,
    var count: Int = 0)

fun calc(expr: String, arg: Int): Long {
    val tokens = expr.split(" ").map { if (it == "old") arg.toString() else it }
    fun rec(from: Int, to: Int): Long {
        if (from + 1 == to) {
            return tokens[from].toLong()
        }
        var i = to - 2
        var cur = tokens[to - 1].toLong()
        while (i >= from && tokens[i] == "*") {
            cur *= tokens[i - 1].toLong()
            i -= 2
        }
        return if (i < from) {
            cur
        } else {
            val rest = rec(from, i)
            when (tokens[i]) {
                "+" -> rest + cur
                "-" -> rest - cur
                else -> error("(")
            }
        }
    }
    return rec(0, tokens.size)
}

tailrec fun gcd(x: Int, y: Int): Int = if (y == 0) x else gcd(y, x % y)
fun lcm(x: Int, y: Int) = x / gcd(x, y) * y

fun solveEasy(lines: List<String>) {
    val monkeys = lines.split("").map { block ->
        Monkey(
            block[1].substringAfter("Starting items: ").split(", ").map(String::toInt).toMutableList(),
            block[2].substringAfter("new = "),
            block[3].substringAfter("divisible by ").toInt(),
            listOf(5, 4).map { block[it].substringAfter("monkey ").toInt() }
        )
    }
    repeat (20) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                val newValue = calc(monkey.operation, item).toInt() / 3
                monkeys[monkey.to[if (newValue % monkey.test == 0) 1 else 0]].items.add(newValue)
                monkey.count += 1
            }
            monkey.items.clear()
        }
    }
    println(monkeys.map { it.count }.sorted().reversed().let {
        it[0] * it[1]
    })
}

fun solveHard(lines: List<String>) {
    val monkeys = lines.split("").map { block ->
        Monkey(
            block[1].substringAfter("Starting items: ").split(", ").map(String::toInt).toMutableList(),
            block[2].substringAfter("new = "),
            block[3].substringAfter("divisible by ").toInt(),
            listOf(5, 4).map { block[it].substringAfter("monkey ").toInt() }
        )
    }
    val totalLcm = monkeys.map { it.test }.fold(1) { x, y -> lcm(x, y) }
    repeat (10000) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                val newValue = (calc(monkey.operation, item) % totalLcm).toInt()
                monkeys[monkey.to[if (newValue % monkey.test == 0) 1 else 0]].items.add(newValue)
                monkey.count += 1
            }
            monkey.items.clear()
        }
    }
    println(monkeys.map { it.count }.sorted().reversed().let {
        1L * it[0] * it[1]
    })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}