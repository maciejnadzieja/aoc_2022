class Day7 {
    fun run() {
        // val lines = """
        //         $ cd /
        //         $ ls
        //         dir a
        //         14848514 b.txt
        //         8504156 c.dat
        //         dir d
        //         $ cd a
        //         $ ls
        //         dir e
        //         29116 f
        //         2557 g
        //         62596 h.lst
        //         $ cd e
        //         $ ls
        //         584 i
        //         $ cd ..
        //         $ cd ..
        //         $ cd d
        //         $ ls
        //         4060174 j
        //         8033020 d.log
        //         5626152 d.ext
        //         7214296 k
        // """.trimIndent().split("\n")

        val lines = Day7::class.java.getResource("/day7.txt")!!.readText().split("\n").filter { it.isBlank() }

        var path = mutableListOf<String>()
        var sizes = mutableMapOf<String, Int>()
        val subdirectories = mutableMapOf<String, Set<String>>()

        for (line in lines.map { it.trim() }) {
            if (line.startsWith("$")) {
                if (line == "$ cd /") {
                    path = mutableListOf("/")
                } else if (line == "$ cd ..") {
                    path.removeLast()
                } else if (line.startsWith("$ cd ")) {
                    path.add(line.replace("$ cd ", ""))
                } else if (line == "$ ls") {
                    val pathString = path.joinToString("/").replace("//", "/")
                    sizes[pathString] = 0
                } else {
                    println("unknown: $line")
                }
            } else if (!line.startsWith("dir")) {
                for (i in 1 .. path.size) {
                    val pathString = path.subList(0, i).joinToString("/").replace("//", "/")
                    sizes[pathString] = sizes.getValue(pathString) + line.split(" ")[0].toInt()
                }
            } else if (line.startsWith("dir")) {
                val pathString = path.joinToString("/").replace("//", "/")
                subdirectories.putIfAbsent(pathString, emptySet())
                subdirectories[pathString] = subdirectories[pathString]!!.plus(line.replace("dir ", ""))
            }
        }
        println(sizes)
        val unused = 70000000 - sizes["/"]!!
        println(unused)
        val toFree = 30000000 - unused
        println(toFree)
        println((sizes.toList().sortedBy { (_, value) -> value}).first {it.second > toFree})
        //println(sizes.filter { it.value <= 100000 }.map { it.value }.sum())
    }
}

fun main(args: Array<String>) {
    Day7().run()
}