fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 10, sample = false)

    val cycles = mutableMapOf(1 to 1)
    var currentCycle = 1
    var currentRegistryValue = 1
    for (line in lines) {
        if (line == "noop") {
            currentCycle++
            cycles[currentCycle] = currentRegistryValue
        } else if (line.startsWith("addx")) {
            val modifier = line.split(" ")[1].toInt()
            currentCycle++
            cycles[currentCycle] = currentRegistryValue
            currentCycle++
            currentRegistryValue += modifier
            cycles[currentCycle] = currentRegistryValue
        } else throw RuntimeException("unknown instruction")
    }

    val expectedCycles = listOf(20, 60, 100, 140, 180, 220)
    println(cycles.filter { expectedCycles.contains(it.key) }.map { it.key * it.value }.sum())

    for (row in 0..5) {
        for (col in 0..39) {
            val cycle = row * 40 + col + 1
            val cycleValue = cycles[cycle]!!
            val sprite = listOf(cycleValue - 1, cycleValue, cycleValue + 1)
            if (sprite.contains(col)) {
                print("#")
            } else {
                print(".")
            }
        }
        println("")
    }
}