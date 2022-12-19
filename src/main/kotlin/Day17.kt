import java.math.BigInteger

fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 17, sample = false)
    val jets = lines.first().toList().filter { it == '<' || it == '>' }

    println("part1, height after 2022: ${chamberAfter(2022, Chamber(), rocks, jets).height}")
    val patterns = mutableMapOf<String, MutableList<Int>>()
    chambers(5000, Chamber(), rocks, jets).forEachIndexed { i, chamber ->
        val chamberString = chamber.toString()
        patterns.putIfAbsent(chamberString, mutableListOf())
        patterns[chamberString]!!.add(i)
    }
    val pattern = patterns.filter { it.value.size > 1 }.firstNotNullOf { it.value }
    val patternRepeatAfterSteps = pattern[1] - pattern[0]
    val heightDiff = chamberAfter(pattern[1], Chamber(), rocks, jets).height -
        chamberAfter(pattern[0], Chamber(), rocks, jets).height
    val numberOfRocksRequestedByElephants = BigInteger("1000000000000")
    val stepsAfterLastPattern = numberOfRocksRequestedByElephants.mod(BigInteger(patternRepeatAfterSteps.toString()))
    val heightAfterLastPattern = chamberAfter(stepsAfterLastPattern.toInt(), Chamber(), rocks, jets).height
    val heightFromProp =
        numberOfRocksRequestedByElephants.divide(BigInteger(patternRepeatAfterSteps.toString()))
            .multiply(BigInteger(heightDiff.toString()))

    println(
        "part2, height after ${numberOfRocksRequestedByElephants}: ${
            heightFromProp + BigInteger(
                heightAfterLastPattern.toString()
            )
        }"
    )
}

fun chamberAfter(rocksNum: Int, initial: Chamber, rocks: List<List<Row>>, jets: List<Char>): Chamber {
    return chambers(rocksNum, initial, rocks, jets).last()
}

fun chambers(rocksNum: Int, initial: Chamber, rocks: List<List<Row>>, jets: List<Char>): List<Chamber> {
    var nextJet = 0
    val chambers = mutableListOf<Chamber>()
    var chamber = initial
    for (i in 0 until rocksNum) {
        val rock = rocks[i % rocks.size]
        chamber = chamber.addRock(rock)
        do {
            val jet = jets[nextJet++ % jets.size]
            chamber = chamber.moveJet(jet)
            if (chamber.rocksCanFall()) {
                chamber = chamber.moveDown()
            } else {
                chamber = chamber.mergeRocks()
                break
            }
        } while (true)
        chambers.add(chamber.copy())
    }
    return chambers
}

data class Chamber(val rows: List<Row> = listOf(Row(), Row(), Row()), val height: Int = 0) {
    fun addRock(rock: List<Row>): Chamber {
        return Chamber(rows.plus(rock.reversed()), height)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        rows.reversed().forEach { sb.appendLine("|${it.columns}|") }
        sb.appendLine("+-------+")
        return sb.toString()
    }

    fun moveJet(jet: Char): Chamber {
        return if (rows.all { it.canApplyJet(jet) }) {
            Chamber(rows.map { it.applyJet(jet) }, height)
        } else this
    }

    fun moveDown(): Chamber {
        val firstRowWithRock = rows.indexOfFirst { it.columns.contains('@') }
        var row = firstRowWithRock
        val rock = mutableListOf<Pair<Int, Int>>()
        do {
            rock.addAll(
                rows[row].columns.mapIndexed { idx, c -> row to if (c == '@') idx else Int.MAX_VALUE }
                    .filter { it.second != Int.MAX_VALUE }
                    .toList()
            )
            row++
        } while (row < rows.size && rows[row].columns.contains('@'))
        return Chamber(
            rows.mapIndexed { rx, r ->
                Row(r.columns.toList().mapIndexed { cx, col ->
                    if (rock.contains(rx + 1 to cx)) {
                        '@'
                    } else if (rock.contains(rx to cx)) {
                        '.'
                    } else {
                        col
                    }
                }.joinToString(""))
            },
            height
        )
    }

    fun rocksCanFall(): Boolean {
        if (rows.first().columns.contains('@')) return false
        for (row in 1 until rows.size) {
            for (col in 0 until rows[row].columns.length) {
                if (rows[row].columns[col] == '@' && rows[row - 1].columns[col] == '#') {
                    return false
                }
            }
        }
        return true
    }

    fun mergeRocks(): Chamber {
        val newRows = rows.map { Row(it.columns.replace('@', '#')) }
            .filterNot { it.isEmpty() }
            .plus(listOf(Row(), Row(), Row()))

        return Chamber(
            newRows.takeLast(100),
            height + newRows.size - 3 - rows.count { it.columns.contains('#') }
        )
    }
}

class Row(val columns: String = ".......") {
    private val width: Int = 7

    init {
        assert(columns.length == width)
    }

    fun isEmpty(): Boolean {
        return columns.all { it == '.' }
    }

    fun canApplyJet(jet: Char): Boolean {
        return !columns.toList().contains('@') || columns.all {
            when (jet) {
                '>' -> {
                    val cols = columns.toList()
                    cols.lastIndexOf('@') < width - 1
                        && cols[cols.lastIndexOf('@') + 1] == '.'
                }

                '<' -> {
                    val cols = columns.toList()
                    cols.indexOfFirst { it == '@' } > 0
                        && cols[cols.indexOfFirst { it == '@' } - 1] == '.'
                }

                else -> true
            }
        }
    }

    fun applyJet(jet: Char): Row {
        if (!columns.contains('@')) {
            return Row(columns)
        }
        return when (jet) {
            '>' -> {
                Row(columns.toList().mapIndexed { idx, c ->
                    if (c == '#') '#'
                    else if (idx > 0 && columns[idx - 1] == '@') {
                        '@'
                    } else {
                        '.'
                    }
                }.joinToString(""))
            }

            '<' -> {
                Row(columns.toList().mapIndexed { idx, c ->
                    if (c == '#') '#'
                    else if (idx < width - 1 && columns[idx + 1] == '@') {
                        '@'
                    } else {
                        '.'
                    }
                }.joinToString(""))
            }

            else -> this
        }
    }

    override fun toString(): String {
        return columns
    }
}

val rocks = listOf(
    listOf(
        Row("..@@@@.")
    ),
    listOf(
        Row("...@..."),
        Row("..@@@.."),
        Row("...@...")
    ),
    listOf(
        Row("....@.."),
        Row("....@.."),
        Row("..@@@..")
    ),
    listOf(
        Row("..@...."),
        Row("..@...."),
        Row("..@...."),
        Row("..@....")
    ),
    listOf(
        Row("..@@..."),
        Row("..@@...")
    )
)