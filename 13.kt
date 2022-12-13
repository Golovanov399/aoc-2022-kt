import java.io.File

open class Structure

data class ListStructure(val items: List<Structure>) : Structure()
data class IntStructure(val value: Int) : Structure()

fun cmp(a: Structure, b: Structure): Int {
    if (a is IntStructure && b is IntStructure) {
        return a.value.compareTo(b.value)
    }
    val aItems = if (a is IntStructure) listOf(a) else (a as ListStructure).items
    val bItems = if (b is IntStructure) listOf(b) else (b as ListStructure).items
    for ((x, y) in aItems.zip(bItems)) {
        val res = cmp(x, y)
        if (res != 0) {
            return res
        }
    }
    return aItems.size.compareTo(bItems.size)
}

class Parser(private val expr: String) {
    var i: Int = 0

    private fun parseInt(): IntStructure {
        var x = 0
        while (i < expr.length && expr[i].isDigit()) {
            x = x * 10 + expr[i].digitToInt()
            i += 1
        }
        return IntStructure(x)
    }

    private fun parseList(): ListStructure {
        check(expr[i] == '[')
        val items = mutableListOf<Structure>()
        while (expr[i] in "[,") {
            i += 1
            if (expr[i] == ']') {
                break
            }
            items.add(parseExpr())
        }
        i += 1
        return ListStructure(items)
    }

    fun parseExpr(): Structure = if (expr[i].isDigit()) parseInt() else parseList()
}

fun solveEasy(lines: List<String>) {
    println(lines.split("").withIndex().filter { (idx, rows) ->
        check(rows.size == 2)
        val structures = rows.map { Parser(it).parseExpr() }
        cmp(structures[0], structures[1]) == -1
    }.sumOf { it.index + 1 })
}

fun solveHard(lines: List<String>) {
    val added = listOf("[[2]]", "[[6]]")
    val trees = lines
        .asSequence()
        .filter { it != "" }
        .plus(added)
        .map { Parser(it).parseExpr() }
        .withIndex()
        .sortedWith { a, b -> cmp(a.value, b.value) }
        .toList()
    println((trees.size - 2 until trees.size).map { idx -> trees.indexOfFirst { it.index == idx } }.reduce { x, y -> (x + 1) * (y + 1) })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}