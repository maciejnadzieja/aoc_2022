fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 18, sample = false)
    val cubes = lines.map {
        val cords = it.split(",")
        Cube(cords[0].toInt(), cords[1].toInt(), cords[2].toInt())
    }

    val notConnectedSides = cubes.map { cube ->
        cube.neighbours().map { n ->
            if (cubes.contains(n)) {
                0
            } else {
                1
            }
        }.sum()
    }.sum()

    println("part1, not connected sides: $notConnectedSides")
    val potentialAirPockets = cubes.map { cube -> cube.neighbours() }.flatten().filterNot { cubes.contains(it) }
    val cubesTrappedWithin = potentialAirPockets.filter { cubes.containsAll(it.neighbours()) }

    println("part2, exterior surface area: ${notConnectedSides - 6 * cubesTrappedWithin.size}")
}

data class Cube(val x: Int, val y: Int, val z: Int) {
    fun neighbours(): List<Cube> {
        return listOf(
            Cube(x - 1, y, z),
            Cube(x + 1, y, z),
            Cube(x, y - 1, z),
            Cube(x, y + 1, z),
            Cube(x, y, z - 1),
            Cube(x, y, z + 1),
        )
    }
}