fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 14, sample = true)
    var cave = prepareCave(lines)
    //part1
    var sandUnitsDropped = 0
    do {
        sandUnitsDropped++
        cave = cave.dropSand()
    } while (!cave.flowToAbyss)
    println(cave)
    println(sandUnitsDropped - 1)
    //part2
    val floor = cave.bottom + 2
    val left = cave.leftWall - floor
    val right = cave.rightWall + floor
    cave = prepareCave(lines + "$left,$floor -> $right,$floor")
    sandUnitsDropped = 0
    do {
        sandUnitsDropped++
        cave = cave.dropSand()
    } while (!cave.pouringPointReached)
    println(cave)
    println(sandUnitsDropped)
}

class Cave(private val map: Map<Point, String>, val bottom: Int) {
    private val pouringPoint = Point(500, 0)
    val pouringPointReached = map.containsKey(pouringPoint)
    val leftWall = map.keys.minOfOrNull { it.x }!!
    val rightWall = map.keys.maxOfOrNull { it.x }!!
    val flowToAbyss = map.keys.map { it.y }.maxOf { it } > bottom

    fun dropSand(): Cave {
        var currentPoint = pouringPoint
        var atRest = false
        while (!atRest) {
            if (currentPoint.y > bottom) {
                return Cave(map.plus(currentPoint to "~"), bottom)
            }
            val nextPoint = listOf(
                currentPoint.down(),
                currentPoint.downLeft(),
                currentPoint.downRight()
            ).firstOrNull { !map.containsKey(it) }
            if (nextPoint != null) {
                currentPoint = nextPoint
                continue
            }

            if (currentPoint == pouringPoint) {
                return Cave(map.plus(currentPoint to "o"), bottom)
            }
            atRest = true
        }
        return Cave(map.plus(currentPoint to "o"), bottom)
    }

    override fun toString(): String {
        val caveBottom = map.keys.map { it.y }.maxOf { it }
        val caveLeftWall = map.keys.map { it.x }.minOf { it }
        val caveRightWall = map.keys.map { it.x }.maxOf { it }
        val sb = StringBuilder()
        for (row in 0..caveBottom) {
            for (col in caveLeftWall..caveRightWall) {
                if (map.containsKey(Point(col, row))) {
                    sb.append(map[Point(col, row)])
                } else {
                    sb.append(".")
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}

private fun prepareCave(
    lines: List<String>
): Cave {
    val cave = mutableMapOf<Point, String>()
    lines.forEach { line ->
        line.split(" -> ")
            .map { Point(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }
            .zipWithNext()
            .forEach { rockLine ->
                if (rockLine.first.x == rockLine.second.x) { //vertical
                    val start = minOf(rockLine.first.y, rockLine.second.y)
                    val stop = maxOf(rockLine.first.y, rockLine.second.y)
                    for (i in IntRange(start, stop)) {
                        cave[Point(rockLine.first.x, i)] = "#"
                    }
                } else if (rockLine.first.y == rockLine.second.y) { //horizontal
                    val start = minOf(rockLine.first.x, rockLine.second.x)
                    val stop = maxOf(rockLine.first.x, rockLine.second.x)
                    for (i in IntRange(start, stop)) {
                        cave[Point(i, rockLine.first.y)] = "#"
                    }
                } else {
                    throw RuntimeException("unknown rock line $rockLine")
                }
            }
    }
    return Cave(cave.toMap(), bottom = cave.keys.map { it.y }.maxOf { it })
}

data class Point(val x: Int, val y: Int) {
    fun down(): Point {
        return Point(x, y + 1)
    }

    fun downLeft(): Point {
        return Point(x - 1, y + 1)
    }

    fun downRight(): Point {
        return Point(x + 1, y + 1)
    }
}