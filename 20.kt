import java.io.File
import kotlin.random.Random

data class Treap(
    val key: Int,
    val priority: Int = Random.nextInt(),
    var left: Treap? = null,
    var right: Treap? = null,
    var parent: Treap? = null,
    var size: Int = 1
) {
    fun leftSize() = left?.size ?: 0
    fun rightSize() = right?.size ?: 0

    fun recalc() {
        left?.parent = this
        right?.parent = this
        size = 1 + leftSize() + rightSize()
    }

    fun getIndex(): Int {
        var node = this
        var ans = leftSize()
        while (node.parent != null) {
            val parent = node.parent
            if (parent!!.right == node) {
                ans += 1 + parent.leftSize()
            }
            node = parent
        }
        return ans
    }
}

fun merge(a: Treap?, b: Treap?): Treap? {
    if (a == null) {
        return b
    }
    if (b == null) {
        return a
    }
    return if (a.priority < b.priority) {
        a.right = merge(a.right, b)
        a.recalc()
        a
    } else {
        b.left = merge(a, b.left)
        b.recalc()
        b
    }
}

fun splitBySize(a: Treap?, leftSize: Int): Pair<Treap?, Treap?> {
    if (a == null) {
        return Pair(null, null)
    }
    return if (a.leftSize() >= leftSize) {
        val (x, y) = splitBySize(a.left, leftSize)
        a.left = y
        a.recalc()
        Pair(x, a)
    } else {
        val (x, y) = splitBySize(a.right, leftSize - a.leftSize() - 1)
        a.right = x
        a.recalc()
        Pair(a, y)
    }
}

fun solveEasy(lines: List<String>) {
    val a = lines.map { Treap(it.toInt()) }
    var root = a.reduce { x, y -> merge(x, y)!! }
    a.forEach { node ->
        val need = node.key % (a.size - 1)
        val idx = node.getIndex()
        val (left, rightWithUs) = splitBySize(root, idx)
        val (us, right) = splitBySize(rightWithUs, 1)
        check(us == node)
        when {
            need > 0 -> {
                val rightSize = right?.size ?: 0
                root = if (need <= rightSize) {
                    val (x, y) = splitBySize(right, need)
                    merge(left, merge(x, merge(us, y)))!!
                } else {
                    val (x, y) = splitBySize(left, need - rightSize)
                    merge(x, merge(us, merge(y, right)))!!
                }
            }
            need < 0 -> {
                val leftSize = left?.size ?: 0
                root = if (-need <= leftSize) {
                    val (x, y) = splitBySize(left, leftSize + need)
                    merge(x, merge(us, merge(y, right)))!!
                } else {
                    val (x, y) = splitBySize(right, (right?.size ?: 0) + need + leftSize)
                    merge(left, merge(x, merge(us, y)))!!
                }
            }
            else -> {
                root = merge(left, merge(us, right))!!
            }
        }
    }
    fun getKth(k: Int): Int {
        val (left, rightWithUs) = splitBySize(root, k)
        val (us, right) = splitBySize(rightWithUs, 1)
        root = merge(left, merge(us, right))!!
        return us!!.key
    }
    val idx = a.single { it.key == 0 }.getIndex()
    println(listOf(1000, 2000, 3000).sumOf { jump -> getKth((idx + jump) % a.size) })
}

fun solveHard(lines: List<String>) {
    val decryptionKey = 811589153
    val a = lines.map { Treap(it.toInt()) }
    var root = a.reduce { x, y -> merge(x, y)!! }
    repeat (10) {
        a.forEach { node ->
            val need = (1L * node.key * decryptionKey % (a.size - 1)).toInt()
            val idx = node.getIndex()
            val (left, rightWithUs) = splitBySize(root, idx)
            val (us, right) = splitBySize(rightWithUs, 1)
            check(us == node)
            when {
                need > 0 -> {
                    val rightSize = right?.size ?: 0
                    root = if (need <= rightSize) {
                        val (x, y) = splitBySize(right, need)
                        merge(left, merge(x, merge(us, y)))!!
                    } else {
                        val (x, y) = splitBySize(left, need - rightSize)
                        merge(x, merge(us, merge(y, right)))!!
                    }
                }
                need < 0 -> {
                    val leftSize = left?.size ?: 0
                    root = if (-need <= leftSize) {
                        val (x, y) = splitBySize(left, leftSize + need)
                        merge(x, merge(us, merge(y, right)))!!
                    } else {
                        val (x, y) = splitBySize(right, (right?.size ?: 0) + need + leftSize)
                        merge(left, merge(x, merge(us, y)))!!
                    }
                }
                else -> {
                    root = merge(left, merge(us, right))!!
                }
            }
        }
    }
    fun getKth(k: Int): Int {
        val (left, rightWithUs) = splitBySize(root, k)
        val (us, right) = splitBySize(rightWithUs, 1)
        root = merge(left, merge(us, right))!!
        return us!!.key
    }
    val idx = a.single { it.key == 0 }.getIndex()
    println(listOf(1000, 2000, 3000).sumOf { jump -> getKth((idx + jump) % a.size) } * 1L * decryptionKey)
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}