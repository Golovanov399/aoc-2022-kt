import java.io.File
import kotlin.math.abs
import kotlin.math.min

data class Rhomb(val x: Int, val y: Int, val radius: Int)

fun solveEasy(lines: List<String>) {
    val rhombs = lines.map { line ->
        val sensorX = line.substringAfter("x=").takeWhile { it in "0123456789-" }.toInt()
        val sensorY = line.substringAfter("y=").takeWhile { it in "0123456789-" }.toInt()
        val beaconX = line.substringAfterLast("x=").takeWhile { it in "0123456789-" }.toInt()
        val beaconY = line.substringAfterLast("y=").takeWhile { it in "0123456789-" }.toInt()
        Rhomb(sensorX, sensorY, abs(beaconX - sensorX) + abs(beaconY - sensorY))
    }
    val beacons = lines.map { line ->
        val beaconX = line.substringAfterLast("x=").takeWhile { it in "0123456789-" }.toInt()
        val beaconY = line.substringAfterLast("y=").takeWhile { it in "0123456789-" }.toInt()
        beaconX to beaconY
    }.toSet()
    fun countForY(y: Int): Int {
        val segments = rhombs.mapNotNull { rhomb ->
            if (abs(rhomb.y - y) > rhomb.radius) {
                null
            } else {
                val xRadius = rhomb.radius - abs(rhomb.y - y)
                rhomb.x - xRadius to rhomb.x + xRadius
            }
        }.sortedBy { it.first }
        var ans = 0
        var last = Int.MIN_VALUE
        segments.forEach { (l, r) ->
            if (r <= last) {
                return@forEach
            }
            if (l > last) {
                last = l - 1
            }
            ans += r - last
            last = r
        }
        ans -= beacons.count { it.second == y }
        return ans
    }
    println(countForY(2_000_000))
}

fun solveHard(lines: List<String>) {
    val rhombs = lines.map { line ->
        val sensorX = line.substringAfter("x=").takeWhile { it in "0123456789-" }.toInt()
        val sensorY = line.substringAfter("y=").takeWhile { it in "0123456789-" }.toInt()
        val beaconX = line.substringAfterLast("x=").takeWhile { it in "0123456789-" }.toInt()
        val beaconY = line.substringAfterLast("y=").takeWhile { it in "0123456789-" }.toInt()
        Rhomb(sensorX, sensorY, abs(beaconX - sensorX) + abs(beaconY - sensorY))
    }
    val (lowerBound, upperBound) = 0 to 4_000_000
    fun findSkippedForY(y: Int): Int? {
        val segments = rhombs.mapNotNull { rhomb ->
            if (abs(rhomb.y - y) > rhomb.radius) {
                null
            } else {
                val xRadius = rhomb.radius - abs(rhomb.y - y)
                rhomb.x - xRadius to rhomb.x + xRadius
            }
        }.sortedBy { it.first }
        var last = lowerBound
        segments.forEach { (l, r) ->
            if (r <= last) {
                return@forEach
            }
            if (l > last + 1) {
                return last + 1
            }
            last = min(r, upperBound)
        }
        if (last < upperBound) {
            return last + 1
        }
        return null
    }
    println((lowerBound..upperBound).mapNotNull { y ->
        val x = findSkippedForY(y)
        if (x != null) {
            x to y
        } else {
            null
        }
    }.single().let { (x, y) -> 1L * x * upperBound + y })
}

fun main() {
    val input = File("src/main/kotlin", "in.in").readLines()
    solveEasy(input)
    solveHard(input)
}