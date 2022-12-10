class Day3 {

    val scores = mutableMapOf<Char, Int>().apply {
        var c: Char = 'a'
        var i: Int = 1

        while (c <= 'z') {
            set(c, i)
            ++c
            ++i
        }

        c = 'A'

        while (c <= 'Z') {
            set(c, i)
            ++c
            ++i
        }
    }

    fun run() {
        val lines = Day3::class.java.getResource("/day3.txt")!!.readText().split("\n").filter { it.isNotBlank() }
        // val lines = """
        //     vJrwpWtwJgWrhcsFMMfFFhFp
        //     jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        //     PmmdzqPrVvPwwTWBwg
        //     wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        //     ttgJtRGJQctTZtZT
        //     CrZsJsPPZsGzwwsLwLmpwMDw
        // """.trimIndent().split("\n")

        val chunkedLines = lines.chunked(3)

        var sum = 0
        for (line3 in chunkedLines) {
            val firstRucksack = line3[0].toList()
            val secondRucksack = line3[1].toList()
            val thirdRucksack = line3[2].toList()
            val commonItem = firstRucksack.intersect(secondRucksack.toSet()).intersect(thirdRucksack.toSet()).first()
            sum += scores[commonItem]!!
        }
        println(sum)
    }
}

fun main(args: Array<String>) {
    Day3().run()
}