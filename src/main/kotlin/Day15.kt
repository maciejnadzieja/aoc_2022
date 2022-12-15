import java.math.BigDecimal
import kotlin.math.abs

fun main(args: Array<String>) {
    // val sample = true
    // val min = 0
    // val max = 20
    // val rowToAnalyze = 10
    val sample = false
    val min = 0
    val max = 4000000
    val rowToAnalyze = 2000000
    val lines = InputParser.getLines(day = 15, sample = sample)
    val sensorsAndBeacons = lines.associate {
        val match =
            "Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)".toRegex().find(it)!!
        Pos(match.destructured.component1().toInt(), match.destructured.component2().toInt()) to
            Pos(match.destructured.component3().toInt(), match.destructured.component4().toInt())
    }
    val positionsOnRow = sensorsAndBeacons.mapNotNull { (s, b) ->
        TaxicabCircle(s, s.distance(b)).rangeOnRow(rowToAnalyze)?.toList()
    }.flatten().distinct() - 1
    println("positions that cannot contain a beacon on row $rowToAnalyze: ${positionsOnRow.size}")
    val distressBeacon = mutableListOf<Pos>()
    for (row in min..max) {
        val rangesOnRow = sensorsAndBeacons.mapNotNull { (sensor, beacon) ->
            TaxicabCircle(sensor, sensor.distance(beacon)).rangeOnRow(row, min, max)
        }
        val freeX =
            rangesOnRow.map { it.last + 1 }.filter { it <= max }.filter { x -> rangesOnRow.none { it.contains(x) } }
        if (freeX.isNotEmpty()) {
            distressBeacon.addAll(freeX.distinct().map { Pos(it, row) })
            break
        }
    }
    println(distressBeacon)
    val tuningFrequency = distressBeacon.first().let { BigDecimal(it.x) * BigDecimal(4000000) + BigDecimal(it.y) }
    println("tuningFrequency = $tuningFrequency")
}

data class TaxicabCircle(val center: Pos, val radius: Int) {
    fun rangeOnRow(row: Int, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntRange? {
        val yDiff = abs(center.y - row)
        val firstX = maxOf(center.x - radius + yDiff, min)
        val lastX = minOf(center.x + radius - yDiff, max)
        return if (firstX <= lastX) firstX..lastX else null
    }
}

data class Pos(val x: Int, val y: Int) {
    fun distance(other: Pos): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}