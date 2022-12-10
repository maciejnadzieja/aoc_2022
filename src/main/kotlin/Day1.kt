class Day1 {
    fun run() {
        val lines = Day1::class.java.getResource("/day1.txt")!!.readText().split("\n")
        var elf = 1
        val caloriesPerElf = mutableMapOf<Int, Int>()

        for (line in lines) {
            if (line.isNotBlank()) {
                caloriesPerElf[elf] = caloriesPerElf.getOrDefault(elf, 0) + line.toInt()
            } else {
                elf += 1
            }
        }

        val sorted = caloriesPerElf.toSortedMap(compareByDescending { caloriesPerElf[it] }).toList().take(3)
        println(sorted.sumOf { it.second })
    }
}

//{53=69289, 227=68321, 10=68005


fun main(args: Array<String>) {
    Day1().run()
}