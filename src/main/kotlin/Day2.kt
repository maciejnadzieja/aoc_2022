class Day2 {
    fun run() {
        val lines = Day2::class.java.getResource("/day2.txt")!!.readText().split("\n").filter { it.isNotBlank() }
        //val lines = listOf("A Y", "B X", "C Z")
        //X - loose Y - draw Z - win
        //rock paper scissors

        var sum = 0

        for (line in lines) {
            val first = line.split(" ")[0]
            val secondResult = line.split(" ")[1]

            val second = gameResultToItem(first, secondResult)

            sum += gameScore(first, second)
            sum+= if (second == "X") 1 else if (second == "Y") 2 else if (second == "Z") 3 else throw Exception("only XYZ allowed")
        }

        println(sum)
    }

    fun gameResultToItem(first: String, second: String): String {
        assert(listOf("A", "B", "C").contains(first))
        assert(listOf("X", "Y", "Z").contains(second))
        return when (first to second) {
            "A" to "X" -> "Z" //rock //X - loose Y - draw Z - win
            "A" to "Y" -> "X"
            "A" to "Z" -> "Y"

            "B" to "X" -> "X" //paper
            "B" to "Y" -> "Y"
            "B" to "Z" -> "Z"

            "C" to "X" -> "Y" //scissors
            "C" to "Y" -> "Z"
            "C" to "Z" -> "X"
            else -> throw Exception("unknown sides")
        }
    }

    fun gameScore(first: String, second: String): Int {
        assert(listOf("A", "B", "C").contains(first))
        assert(listOf("X", "Y", "Z").contains(second))
        return when (first to second) {
            "A" to "X" -> 3 //rock paper scissors
            "A" to "Y" -> 6
            "A" to "Z" -> 0

            "B" to "X" -> 0
            "B" to "Y" -> 3
            "B" to "Z" -> 6

            "C" to "X" -> 6
            "C" to "Y" -> 0
            "C" to "Z" -> 3
            else -> throw Exception("unknown game sides")
        }
    }
}

fun main(args: Array<String>) {
    Day2().run()
}