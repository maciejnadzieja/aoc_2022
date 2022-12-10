class Day8 {
    fun run() {
        val lines = InputParser.getLines(day = 8, sample = true)

        val rowsCount = lines.size
        val columnsCount = lines[0].length

        val trees = lines.mapIndexed { y, line ->
            line.toList().mapIndexed { x, height ->
                (x to y) to height.digitToInt()
            }
        }.flatten()

        var visible = 0
        var maxScore = 0
        for (col in 0 until columnsCount) {
            for (row in 0 until rowsCount) {
                val tree = trees.first { it.first == (col to row) }
                val height = tree.second

                val treesToTheTop = trees.filter { it.first.first == col && it.first.second < row }.reversed()
                val treesToTheBottom = trees.filter { it.first.first == col && it.first.second > row }
                val treesToTheLeft = trees.filter { it.first.second == row && it.first.first < col }.reversed()
                val treesToTheRight = trees.filter { it.first.second == row && it.first.first > col }

                val topScore = score(treesToTheLeft, height)
                val bottomScore = score(treesToTheRight, height)
                val leftScore = score(treesToTheTop, height)
                val rightScore = score(treesToTheBottom, height)

                val score = leftScore * rightScore * topScore * bottomScore
                if (score > maxScore) {
                    maxScore = score
                }

                val leftMaxHeight = treesToTheLeft.maxOfOrNull { it.second } ?: 0
                val rightMaxHeight = treesToTheRight.maxOfOrNull { it.second } ?: 0
                val topMaxHeight = treesToTheTop.maxOfOrNull { it.second } ?: 0
                val bottomMaxHeight = treesToTheBottom.maxOfOrNull { it.second } ?: 0
                if (isOnEdge(tree, columnsCount, rowsCount) || height > leftMaxHeight
                    || height > rightMaxHeight
                    || height > topMaxHeight
                    || height > bottomMaxHeight
                ) {
                    visible++
                }
            }
        }

        println("maxScore=$maxScore")
        println("visible=$visible")
    }

    private fun isOnEdge(tree: Pair<Pair<Int, Int>, Int>, columnsCount: Int, rowsCount: Int): Boolean {
        return tree.first.first == 0
            || tree.first.second == columnsCount - 1
            || tree.first.second == 0
            || tree.first.second == rowsCount - 1
    }

    private fun score(
        trees: List<Pair<Pair<Int, Int>, Int>>,
        height: Int
    ): Int {
        var score = 0
        for (tree in trees) {
            score++
            if (tree.second >= height) {
                break
            }
        }
        return score
    }
}

fun main(args: Array<String>) {
    Day8().run()
}