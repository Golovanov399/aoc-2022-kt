import java.io.File
import kotlin.math.min

data class Dir(val par: Dir?, val contents: MutableMap<String, Dir> = mutableMapOf(), var sz: Int = 0)

fun buildTree(cmds: List<String>): Dir {
    val root = Dir(null)
    var cur = root
    cmds.forEach { line ->
        val cmd = line.split(" ")
        if (cmd[0] == "$") {
            when (cmd[1]) {
                "ls" -> cur.sz = 0
                "cd" -> {
                    val d = cmd[2]
                    cur = when (d) {
                        "/" -> root
                        ".." -> cur.par?:cur
                        else -> {
                            cur.contents.putIfAbsent(d, Dir(cur))
                            cur.contents[d]!!
                        }
                    }
                }
                else -> error("(")
            }
        } else {
            if (cmd[0] == "dir") {
                cur.contents.putIfAbsent(cmd[1], Dir(cur))
            } else {
                cur.sz += cmd[0].toInt()
            }
        }
    }

    fun rec(v: Dir) {
        v.contents.values.forEach { u ->
            rec(u)
            v.sz += u.sz
        }
    }
    rec(root)
    return root
}

fun solveEasy(lines: List<String>) {
    val root = buildTree(lines)
    var ans = 0
    fun rec(v: Dir) {
        if (v.sz <= 100_000) {
            ans += v.sz
        }
        v.contents.values.forEach { u ->
            rec(u)
        }
    }
    rec(root)
    println(ans)
}

fun solveHard(lines: List<String>) {
    val root = buildTree(lines)
    val need = root.sz - 40_000_000
    var ans = Int.MAX_VALUE
    fun rec(v: Dir) {
        if (v.sz >= need) {
            ans = min(ans, v.sz)
        }
        v.contents.values.forEach { u ->
            rec(u)
        }
    }
    rec(root)
    println(ans)
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}