object InputParser {
    fun getLines(day: Int, skipBlank: Boolean = true, sample: Boolean = false): List<String> {
        val file = if (sample) "/samples/day${day}.txt" else "/day${day}.txt"
        val lines = InputParser::class.java.getResource(file)!!.readText()
            .split("\n")

        if (skipBlank) {
            return lines.filter { it.isNotBlank() }
        }
        return lines
    }
}