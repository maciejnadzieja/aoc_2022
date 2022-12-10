class Day6 {
    fun run() {
        val lines = Day6::class.java.getResource("/day6.txt")!!.readText().trimIndent()
        // val lines = """
        //     nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg
        // """.trimIndent()

        val chars = lines.toList()
        val size = 14
        for (i in 0 .. chars.size) {
            if (chars.subList(i, i + size).toSet().size == size) {
                println(i+size)
                break
            }
        }
    }
}

fun main(args: Array<String>) {
    Day6().run()
}