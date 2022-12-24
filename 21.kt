import java.io.File
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.roundToLong

open class Expression(var calculated: Boolean, var value: Double) {
    open fun calculate() {
        TODO()
    }

    open fun copy(): Expression {
        TODO()
    }

    fun get(): Double {
        if (!calculated) {
            calculated = true
            calculate()
        }
        return value
    }
}

class ConstantExpression(x: Long) : Expression(true, x.toDouble()) {
    override fun copy(): Expression {
        return Expression(true, value)
    }
}

class OperatorExpression(
    val left: Expression,
    val right: Expression,
    private val op: Char
    ) : Expression(false, 0.0) {
    override fun calculate() {
        value = when (op) {
            '+' -> left.get() + right.get()
            '-' -> left.get() - right.get()
            '*' -> left.get() * right.get()
            '/' -> left.get() / right.get()
            else -> error("")
        }
    }

    override fun copy(): Expression = OperatorExpression(left.copy(), right.copy(), op)
}

fun solveEasy(lines: List<String>) {
    val monkeyByName = mutableMapOf<String, Expression>()
    val namedInput = lines.map { it.split(": ") }.associate { it[0] to it[1] }
    fun exprByName(s: String): Expression {
        if (s in monkeyByName) {
            return monkeyByName[s]!!
        }
        val tokens = namedInput[s]!!.split(" ")
        val res = if (tokens.size == 1) {
            ConstantExpression(tokens[0].toLong())
        } else {
            OperatorExpression(exprByName(tokens[0]), exprByName(tokens[2]), tokens[1][0])
        }
        monkeyByName[s] = res
        return res
    }
    namedInput.keys.forEach { exprByName(it) }
    println(monkeyByName["root"]!!.get().roundToLong())
}

fun solveHard(lines: List<String>) {
    val monkeyByName = mutableMapOf<String, Expression>()
    val namedInput = lines.map { it.split(": ") }.associate { it[0] to it[1] }
    fun exprByName(s: String): Expression {
        if (s in monkeyByName) {
            return monkeyByName[s]!!
        }
        val tokens = namedInput[s]!!.split(" ")
        val res = if (tokens.size == 1) {
            ConstantExpression(tokens[0].toLong())
        } else {
            OperatorExpression(exprByName(tokens[0]), exprByName(tokens[2]), if (s != "root") tokens[1][0] else '-')
        }
        monkeyByName[s] = res
        return res
    }
    namedInput.keys.forEach { exprByName(it) }

    fun substitute(x: Long): Double {
        monkeyByName["humn"]!!.value = x.toDouble()
        return monkeyByName["root"]!!.copy().get()
    }

    var left = 1L
    var right = Long.MAX_VALUE / 100
    var leftValue = substitute(left)
    var rightValue = substitute(right)
    check((leftValue < 0) != (rightValue < 0))
    while (right > left) {
        val mid = (left + right) / 2
        val midValue = substitute(mid)
        if (midValue == 0.0) {
            left = mid
            right = mid
            break
        } else if ((midValue < 0) == (leftValue < 0)) {
            left = mid
            leftValue = midValue
        } else {
            right = mid
            rightValue = midValue
        }
    }

    println(left)
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}