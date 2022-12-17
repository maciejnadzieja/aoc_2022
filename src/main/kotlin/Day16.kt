import java.util.LinkedList
import java.util.Queue

fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 16, sample = true)
    val valveRates = mutableMapOf<String, Int>()
    val valveTunnels = mutableMapOf<String, List<String>>()
    lines.forEach { line ->
        val match =
            "Valve ([A-Z]+) has flow rate=([0-9]+); tunnels* leads* to valves* (.*)".toRegex().find(line)!!
        val (valve, rate, tunnels) = match.destructured
        valveRates[valve] = rate.toInt()
        valveTunnels[valve] = tunnels.split(",").map { it.trim() }
    }
    println(valveRates)
    println(valveTunnels)
    val pathLengths = valveRates.keys.associateWith { pathLengths(it, valveTunnels) }
    println(pathLengths)
    val nonZeroValves = valveRates.filter { it.value > 0 }.map { it.key }
    println(nonZeroValves)

    val openedValves = mutableListOf<String>()
    val minutes = 30
    var atValve = "AA"
    var minute = 1
    while (minute < minutes || openedValves.size == nonZeroValves.size) {
        val notOpenedValves = nonZeroValves.minus(openedValves)
        val sum = valveRates.filter { notOpenedValves.contains(it.key) }.map { it.value }.sum()
        println("sum=$sum")
        val goToValve = notOpenedValves.associateWith {
            val pathLength = pathLengths[atValve]!![it]!!
            val x = pathLength.toDouble() * sum / valveRates[it]!!
            println("=> $it : $pathLength : $x")
            x
        }.toList().minByOrNull { it.second }?.first
        if (goToValve != null) {
            println("minute $minute going to valve $goToValve")
            val pathLength = pathLengths[atValve]!![goToValve]!!
            atValve = goToValve
            minute += pathLength + 1
            openedValves.add(atValve)
        } else break
    }
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