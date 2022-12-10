fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 9, sample = false)

    val tailsNum = 9
    val knotsPos = mutableMapOf<Int, Pair<Int, Int>>().apply {
        for (i in 0..tailsNum) { //0 is Head
            put(i, 0 to 0)
        }
    }
    val positionsVisitedByLastTail = mutableSetOf(0 to 0)

    for (line in lines) {
        val whereHeadMoves = line.split(" ")[0]
        val headMovesCount = line.split(" ")[1].toInt()

        for (i in 1..headMovesCount) {
            knotsPos[0] = applySingleHeadMove(knotsPos[0]!!, whereHeadMoves)
            for (j in 1..tailsNum) {
                knotsPos[j] = applyTailMove(knotsPos[j - 1]!!, knotsPos[j]!!)
            }
            positionsVisitedByLastTail.add(knotsPos[tailsNum]!!)
        }
    }
    println(positionsVisitedByLastTail.size)
}

private fun applyTailMove(headPos: Pair<Int, Int>, tailPos: Pair<Int, Int>): Pair<Int, Int> {
    return if (tailPos.adjacent(headPos)) {
        tailPos
    } else if (headPos.first != tailPos.first && headPos.second != tailPos.second) { //diagonal move
        tailPos.area().intersect(headPos.area().toSet()).firstOrNull {
            it.first == headPos.first || it.second == headPos.second
        } ?: tailPos.area().intersect(headPos.area().toSet())
            .first { it.adjacent(headPos) } //had to be added when #knots>1 (2,2) - (0,0)
    } else if (headPos.first == tailPos.first) { //same col
        tailPos.area().intersect(headPos.area().toSet()).first {
            it.first == headPos.first
        }
    } else { //same row
        tailPos.area().intersect(headPos.area().toSet()).first {
            it.second == headPos.second
        }
    }
}

private fun Pair<Int, Int>.area(): List<Pair<Int, Int>> {
    val area = mutableListOf<Pair<Int, Int>>()
    for (x in first - 1..first + 1) {
        for (y in second - 1..second + 1) {
            area.add(x to y)
        }
    }
    return area
}

private fun Pair<Int, Int>.adjacent(pair: Pair<Int, Int>): Boolean {
    return pair.area().contains(this)
}

private fun applySingleHeadMove(
    pos: Pair<Int, Int>,
    whereHeadMoves: String
): Pair<Int, Int> {
    return when (whereHeadMoves) {
        "R" -> pos.let { it.first + 1 to it.second }
        "U" -> pos.let { it.first to it.second + 1 }
        "L" -> pos.let { it.first - 1 to it.second }
        "D" -> pos.let { it.first to it.second - 1 }
        else -> throw RuntimeException("unknown move $whereHeadMoves")
    }
}

