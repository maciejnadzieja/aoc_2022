fun main(args: Array<String>) {
    println(
        simulate(
            lines = InputParser.getLines(day = 11, sample = true),
            roundsNum = 10000
        )
    )
}

fun simulate(lines: List<String>, roundsNum: Int): Long {
    val monkeyItems = mutableMapOf<Int, List<Long>>()
    val monkeyOperations = mutableMapOf<Int, String>()
    val monkeyTests = mutableMapOf<Int, MonkeyTest>()

    for (monkeyInput in lines.chunked(6)) {
        val monkeyNum = monkeyInput[0].split(" ")[1].replace(":", "").toInt()
        monkeyItems[monkeyNum] = monkeyInput[1].replace("  Starting items: ", "")
            .split(" ").map { it.replace(",", "").toLong() }
        monkeyOperations[monkeyNum] = monkeyInput[2].replace("Operation: ", "").trim()
        monkeyTests[monkeyNum] = MonkeyTest(
            test = monkeyInput[3].replace("Test: ", "").trim(),
            monkeyIfTrue = monkeyInput[4].replace("    If true: throw to monkey ", "").toInt(),
            monkeyIfFalse = monkeyInput[5].replace("    If false: throw to monkey ", "").toInt(),
        )
    }

    val monkeyInspections = mutableMapOf<Int, Long>().apply {
        for (monkey in monkeyItems.keys) {
            put(monkey, 0)
        }
    }
    val lcm = monkeyTests.values.map { it.div() }.reduce { acc, x -> acc * x }
    for (round in 1..roundsNum) {
        //println("Round $round")
        for (monkey in monkeyItems.keys) {
            //println("Monkey $monkey")
            for (worryLevel in monkeyItems[monkey]!!) {
                monkeyInspections[monkey] = monkeyInspections[monkey]!! + 1
                //println("Monkey inspects an item with a worry level of $worryLevel")
                val worryLevelAfterOperation = applyOperationToWorryLevel(worryLevel, monkeyOperations[monkey]!!)
                //println("Worry level is now $worryLevelAfterOperation.")
                val worryLevelAfterFun = worryLevelAfterOperation.mod(lcm).toLong()
                //println("Monkey gets bored with item. Worry level is divided by $worryReliefLevel to $worryLevelAfterFun")
                val whereToThrow = monkeyTests[monkey]!!.run(worryLevelAfterFun)
                //println("Item with worry level $worryLevelAfterFun is thrown to monkey $whereToThrow.")
                monkeyItems[whereToThrow] = monkeyItems[whereToThrow]!!.plus(worryLevelAfterFun)
            }
            monkeyItems[monkey] = emptyList()
        }
    }
    println(monkeyInspections)
    val inspections = monkeyInspections.toList().sortedByDescending { it.second }
    return inspections[0].second * inspections[1].second
}

fun applyOperationToWorryLevel(worryLevel: Long, operation: String): Long {
    val op = operation.replace("new = ", "").split(" ")
    val x = if (op[0] == "old") worryLevel else op[0].toLong()
    val y = if (op[2] == "old") worryLevel else op[2].toLong()
    return if (operation.contains("*")) {
        x * y
    } else if (operation.contains("+")) {
        x + y
    } else throw RuntimeException("unknown operation")
}

data class MonkeyTest(
    val test: String,
    val monkeyIfTrue: Int,
    val monkeyIfFalse: Int
) {
    fun div(): Int {
        return test.replace("divisible by ", "").toInt()
    }

    fun run(worryLevel: Long): Int {
        val div = test.replace("divisible by ", "").toLong()
        return if (worryLevel.mod(div) == 0L) monkeyIfTrue else monkeyIfFalse
    }
}
