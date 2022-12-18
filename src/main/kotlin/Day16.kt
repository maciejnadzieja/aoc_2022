import java.lang.Integer.max
import java.lang.Integer.min
import java.util.LinkedList
import java.util.Queue

typealias Minute = Int

fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 16, sample = false)
    val valves = lines.map { line ->
        val match =
            "Valve ([A-Z]+) has flow rate=([0-9]+); tunnels* leads* to valves* (.*)".toRegex().find(line)!!
        val (valve, rate, tunnels) = match.destructured
        Valve(valve, rate.toInt(), tunnels.split(",").map { it.trim() })
    }.associateBy { it.name }

    val nonZeroValves = valves.values.filter { it.rate > 0 }
    val pathLengths = pathLengths(valves.values.toList())
    nonZeroValves.forEach { println(it) }
    pathLengths
        .map { "${it.key.first.name}-${it.key.second.name} ${it.value}" }
        .forEach { println(it) }
    val start = System.currentTimeMillis()
    println(expand(emptyList(), nonZeroValves, pathLengths, valves["AA"]!!))
    println((System.currentTimeMillis() - start) / 1000)
    //printPath(nonZeroValves, pathLengths, valves["AA"]!!)
    //printPath(listOf(valves["DD"]!!, valves["BB"]!!, valves["JJ"]!!, valves["HH"]!!, valves["EE"]!!, valves["CC"]!!), pathLengths, valves["AA"]!!)
    // val pathPrefix = listOf(valves["FV"]!!, valves["FR"]!!, valves["QO"]!!, valves["GJ"]!!, valves["RC"]!!, valves["OF"]!!)
    // val maxPressure = expand(
    //     pathPrefix,
    //     nonZeroValves.minus(pathPrefix),
    //     pathLengths,
    //     valves["AA"]!!)
    // println(maxPressure)
}

private fun expand(path: List<Valve>, left: List<Valve>, pathLengths: Map<Pair<Valve, Valve>, Int>, start: Valve): Int {
    return if (left.isEmpty() ) {
        calculatePath(path, pathLengths, start)
    } else if (pathLength(path, pathLengths) >= 30) {
        calculatePath(path, pathLengths, start)
    } else {
        left.maxOf { expand(path + it, left.minus(it), pathLengths, start) }
    }
}

fun pathLength(path: List<Valve>, pathLengths: Map<Pair<Valve, Valve>, Int>): Int {
    if (path.size < 2) {
        return 0
    }
    var sum = 0
    for (i in 1 until path.size) {
        sum += pathLengths[path[i-1] to path[i]]!!
    }
    return sum
}

private fun calculatePath(valvesOrder: List<Valve>, pathLengths: Map<Pair<Valve, Valve>, Int>, start: Valve): Int {
    val openedValves = mutableListOf<Pair<Valve, Minute>>()
    val valvesToBeOpened = valvesOrder.toMutableList()
    var atValve = start
    var minute = 1

    while (valvesToBeOpened.isNotEmpty()) {
        val element = valvesToBeOpened.removeFirst()
        minute += pathLengths[atValve to element]!!
        openedValves.add(element to minute)
        minute += 1
        atValve = element
    }
    return pathPressure(openedValves)
}

private fun pathPressure(valvesOpenedAt: List<Pair<Valve, Minute>>): Int {
    val pressure = valvesOpenedAt.sumOf { (30 - min(30, it.second)) * it.first.rate }
    if (pressure > 1790) {
        println(valvesOpenedAt.joinToString("-") { it.first.name } + " " + pressure)
    }
    return pressure
}

private fun pathLengths(valves: List<Valve>): Map<Pair<Valve, Valve>, Int> {
    return valves.map { start ->
        valves.map { stop ->
            val tunnels = valves.associate { it.name to it.tunnels }
            (start to stop) to pathLengths(start.name, tunnels)[stop.name]!!
        }
    }.flatten().toMap()
}

private fun pathLengths(start: String, tunnels: Map<String, List<String>>): Map<String, Int> { //BFS
    val queue: Queue<String> = LinkedList(listOf(start))
    val lengths = mutableMapOf(start to 0)
    while (queue.isNotEmpty()) {
        val head = queue.poll()
        tunnels[head]!!.filterNot { lengths.keys.contains(it) }.forEach { t ->
            lengths[t] = lengths[head]!! + 1
            queue.add(t)
        }
    }
    return lengths.toMap()
}

data class Valve(val name: String, val rate: Int, val tunnels: List<String>) {

}