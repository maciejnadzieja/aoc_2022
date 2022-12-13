import java.util.LinkedList
import java.util.Queue

fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 12, sample = false)
    val heightmap = lines.mapIndexed { y, line ->
        line.toList().mapIndexed { x, e ->
            Square(x, y, e, scores[e]!!)
        }
    }.flatten()
    val start = heightmap.first { it.elevationSign == 'S' }
    println("$start => ${minPathToEnd(start, heightmap)}")
    val shortestFromA = heightmap.filter { it.elevation == 1 }
        .map { it to minPathToEnd(it, heightmap) }
        .filter { it.second != null }
        .minByOrNull { it.second!! }!!
    println("${shortestFromA.first} => ${shortestFromA.second}")
}

private fun minPathToEnd(start: Square, heightmap: List<Square>): Int? { //BFS
    val queue: Queue<Square> = LinkedList(listOf(start))
    val visited = mutableListOf(start)
    var possiblePaths = listOf(listOf(start))
    while (queue.isNotEmpty()) {
        val head = queue.poll()
        val nextSteps = head.possibleSteps(heightmap).filter { !visited.contains(it) }
        possiblePaths =
            possiblePaths + possiblePaths.filter { it.last() == head }.map { path -> nextSteps.map { path + it } }
                .flatten()
        nextSteps.forEach { n ->
            queue.add(n)
            visited.add(n)
        }
    }
    val pathsToTheEnd = possiblePaths.filter { p -> p.last() == heightmap.first { it.elevationSign == 'E' } }
    return pathsToTheEnd.minByOrNull { it.size }?.let { it.size - 1 }
}

data class Square(
    val x: Int,
    val y: Int,
    val elevationSign: Char,
    val elevation: Int
) {
    private fun neighbours(heightMap: List<Square>): List<Square> {
        return heightMap.filter {
            (it.x == x - 1 && it.y == y)
                || (it.x == x + 1 && it.y == y)
                || (it.y == y - 1 && it.x == x)
                || (it.y == y + 1 && it.x == x)
        }
    }

    fun possibleSteps(heightMap: List<Square>): List<Square> {
        return this.neighbours(heightMap).filter {
            it.elevation <= elevation + 1
        }
    }

    override fun toString(): String {
        return "$x.$y $elevationSign"
    }
}

val scores = mutableMapOf<Char, Int>().apply {
    var c = 'a'
    var i = 1

    while (c <= 'z') {
        set(c, i)
        ++c
        ++i
    }

    set('S', 1)
    set('E', this['z']!!)
}