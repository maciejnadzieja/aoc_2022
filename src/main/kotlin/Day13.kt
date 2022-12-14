import java.util.Deque
import java.util.LinkedList

fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 13, sample = true)
    val pairs = mutableListOf<Pair<ListPacket, ListPacket>>()
    for (i in 0..lines.size - 2 step 2) {
        pairs.add(parse(lines[i]) to parse(lines[i + 1]))
    }
    //part1
    val rightOrderPairs = mutableListOf<Int>()
    pairs.forEachIndexed { idx, pair ->
        val c = compare(pair.first, pair.second)
        if (c < 0) {
            rightOrderPairs.add(idx + 1)
        }
    }
    println(rightOrderPairs.sum())
    //part2
    val div1 = parse("[[2]]")
    val div2 = parse("[[6]]")

    pairs.add(div1 to div2)
    val packets = pairs.map { it.first } + pairs.map { it.second }
    val decoderIdx = packets.asSequence()
        .sortedWith { a, b -> compare(a, b) }
        .mapIndexed { idx, packet -> idx + 1 to packet }
        .filter { it.second == div1 || it.second == div2 }
        .map { it.first }
        .reduce { acc, x -> acc * x }
    println(decoderIdx)
}

fun compare(x: ListPacket, y: ListPacket): Int {
    val first = x.toList()
    val second = y.toList()
    for (idx in first.indices) {
        val c = if (!second.toList().indices.contains(idx)) {
            1
        } else if (first[idx] is IntPacket && second[idx] is IntPacket) {
            (first[idx] as IntPacket).toInt().compareTo((second[idx] as IntPacket).toInt())
        } else if (first[idx] is IntPacket && second[idx] is ListPacket) {
            compare(ListPacket(listOf(first[idx] as IntPacket)), second[idx] as ListPacket)
        } else if (first[idx] is ListPacket && second[idx] is IntPacket) {
            compare(first[idx] as ListPacket, ListPacket(listOf(second[idx] as IntPacket)))
        } else {
            compare(first[idx] as ListPacket, second[idx] as ListPacket)
        }

        if (c == 0) continue else return c
    }

    return if (second.size > first.size) {
        -1
    } else {
        0
    }
}

fun parse(line: String): ListPacket {
    val stack: Deque<Packet> = LinkedList()
    for (op in lineToOps(line)) {
        if (op.toIntOrNull() != null) {
            val head = stack.pop() as ListPacket
            stack.push(head.add(IntPacket(op.toInt())))
        } else if (op == "[") {
            stack.push(ListPacket(emptyList()))
        } else if (op == "]") {
            val head = stack.pop()
            if (stack.isNotEmpty()) {
                val headNext = stack.pop() as ListPacket
                stack.push(headNext.add(head))
            } else {
                stack.push(head)
            }
        } else {
            throw RuntimeException("unknown op $op")
        }
    }
    return stack.pop() as ListPacket
}

fun lineToOps(line: String): List<String> {
    val ops = mutableListOf<String>()
    val split = line.split(",")
    split.forEach {
        val number = it.replace("[", "").replace("]", "")
        val prefix = it.replace(number, "").replace("]", "").toList().map { c -> c.toString() }
        val suffix = it.replace(number, "").replace("[", "").toList().map { c -> c.toString() }
        ops.addAll((prefix + listOf(number) + suffix).filter { x -> x.isNotBlank() })
    }
    return ops
}

sealed interface Packet
class IntPacket(private val value: Int) : Packet {
    fun toInt(): Int {
        return value
    }
}

class ListPacket(private val value: List<Packet>) : Packet {
    fun add(new: Packet): Packet {
        return ListPacket(value + new)
    }

    fun toList(): List<Packet> {
        return value
    }
}

