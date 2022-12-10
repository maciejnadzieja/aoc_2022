import java.util.Deque
import java.util.LinkedList

class Day5 {
    fun run() {
        //val lines = Day5::class.java.getResource("/day5.txt")!!.readText().split("\n")
        val lines = """
                [D]
            [N] [C]
            [Z] [M] [P]
             1   2   3

            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent().split("\n")

        //println(lines)
        val stacks : Map<Int, Deque<String>> = parseStacks(lines)

        for (line in lines.filter { it.startsWith("move") }) {
            val howMany = line.split(" ")[1].toInt()
            val from = line.split(" ")[3].toInt()
            val to = line.split(" ")[5].toInt()

            val toMove = mutableListOf<String>()
            for (i in 1..howMany) {
                toMove.add(stacks[from]!!.pollLast())
            }
            stacks[to]!!.addAll(toMove.reversed())
        }
        println(stacks.map { it.value.pollLast() })
    }

    private fun parseStacks(lines: List<String>): Map<Int, Deque<String>> {
        var stacksNum = 0
        for (line in lines) {
            if (line.isBlank()) {
                break
            } else if (line.startsWith(" 1 ")) {
                stacksNum = line.trim().split(" ").last().toInt()
            }
        }
        println(stacksNum)
        var skip = 0
        val stacks = mutableMapOf<Int, Deque<String>>().apply {
            for (i in 1.. stacksNum) {
                put(i, LinkedList())
            }
        }
        for (i in 1..stacksNum) {
            val toReverse = mutableListOf<String>()
            for (line in lines) {
                if (line.startsWith(" 1")) {
                    break
                }
                val s = line.substring(skip, skip + 3)
                if (s.isNotBlank()) {
                    toReverse.add(s.replace("[", "").replace("]", ""))
                }
            }
            stacks[i]!!.addAll(toReverse.reversed())
            skip += 4
        }
        return stacks
    }
}

fun main(args: Array<String>) {
    Day5().run()
}