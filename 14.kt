import java.io.File

data class Point(val x: Int, val y: Int)

fun sign(x: Int) = x.coerceIn(-1, 1)

class Sandbox {
    private val used = mutableListOf<MutableList<Boolean>>()
    private var offX = 0

    fun setUsed(x: Int, y: Int) {
        while (y !in used.indices) {
            used.add(mutableListOf())
        }
        check(offX + x >= 0)
        while (offX + x !in used[y].indices) {
            used[y].add(false)
        }
        used[y][offX + x] = true
    }

    fun getUsed(x: Int, y: Int) = y in used.indices && offX + x in used[y].indices && used[y][offX + x]

    fun setOffset(x: Int) {
        val dx = x - offX
        if (dx > 0) {
            used.forEach { it.addAll(0, List(dx) { false }) }
        }
        if (dx < 0) {
            used.forEach {
                repeat(-dx) { _ ->
                    it.removeFirstOrNull()
                }
            }
        }
        offX = x
    }

    fun addWall(wall: String) {
        val pts = wall.split(" -> ").map {
            val (x, y) = it.split(",").map(String::toInt)
            Point(x, y)
        }
        var (x, y) = pts[0]
        setUsed(x, y)
        pts.drop(1).forEach { (nx, ny) ->
            while (x != nx || y != ny) {
                x += sign(nx - x)
                y += sign(ny - y)
                setUsed(x, y)
            }
        }
    }

    val maxY: Int
        get() = used.size - 1

    val maxX: Int
        get() = used.maxOf { it.size } - 1 - offX
}

fun solveEasy(lines: List<String>) {
    val sandbox = Sandbox()
    lines.forEach { sandbox.addWall(it) }

    val maxY = sandbox.maxY

    var ans = 0
    while (true) {
        var (x, y) = 500 to 0
        if (sandbox.getUsed(x, y)) {
            break
        }
        while (y < maxY) {
            var ok = false
            for (dx in listOf(0, -1, 1)) {
                val (nx, ny) = (x + dx) to (y + 1)
                if (!sandbox.getUsed(nx, ny)) {
                    x = nx
                    y = ny
                    ok = true
                    break
                }
            }
            if (!ok) {
                sandbox.setUsed(x, y)
                ans += 1
                break
            }
        }
        if (y >= maxY) {
            break
        }
    }
    println(ans)
}

fun solveHard(lines: List<String>) {
    val sandbox = Sandbox()
    lines.forEach { sandbox.addWall(it) }

    val maxY = sandbox.maxY
    val maxX = sandbox.maxX
    sandbox.setOffset(maxY + 30)
    (-maxY - 20 .. maxX + maxY + 20).forEach { sandbox.setUsed(it, maxY + 2) }

    var ans = 0
    while (true) {
        var (x, y) = 500 to 0
        if (sandbox.getUsed(x, y)) {
            break
        }
        while (true) {
            var ok = false
            for (dx in listOf(0, -1, 1)) {
                val (nx, ny) = (x + dx) to (y + 1)
                if (!sandbox.getUsed(nx, ny)) {
                    x = nx
                    y = ny
                    ok = true
                    break
                }
            }
            if (!ok) {
                sandbox.setUsed(x, y)
                ans += 1
                break
            }
        }
    }
    println(ans)
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}