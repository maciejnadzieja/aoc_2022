class Day4 {

    fun run() {
        val lines = Day4::class.java.getResource("/day4.txt")!!.readText().split("\n").filter { it.isNotBlank() }
        // val lines = """
        //     2-4,6-8
        //     2-3,4-5
        //     5-7,7-9
        //     2-8,3-7
        //     6-6,4-6
        //     2-6,4-8
        // """.trimIndent().split("\n")

        var counter = 0
        for (line in lines) {
            val pair = line.split(",")
            val first = pair[0].split("-")[0].toInt() to pair[0].split("-")[1].toInt()
            val second = pair[1].split("-")[0].toInt() to pair[1].split("-")[1].toInt()

            if (overlap(first, second)) {
                println("$first $second")
                counter++
            }
        }
        println(counter)
    }

    private fun fullyContains(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
        val x = first.first .. first.second
        val y = second.first .. second.second
        return x.intersect(y) == x.toSet() || x.intersect(y) == y.toSet()
    }

    private fun overlap(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
        val x = first.first .. first.second
        val y = second.first .. second.second
        return x.intersect(y).isNotEmpty()
    }
}

fun main(args: Array<String>) {
    Day4().run()
}