fun main(args: Array<String>) {
    val lines = InputParser.getLines(day = 19, sample = true)
    val blueprints = lines.associate { line ->
        val match =
            ("Blueprint ([0-9]+): Each ore robot costs ([0-9]+) ore. " +
                "Each clay robot costs ([0-9]+) ore. " +
                "Each obsidian robot costs ([0-9]+) ore and ([0-9]+) clay. " +
                "Each geode robot costs ([0-9]+) ore and ([0-9]+) obsidian.").toRegex()
                .find(line)!!
        val blueprint = match.destructured.component1().toInt()
        blueprint to RobotCosts(
            ore = Cost(ore = match.destructured.component2().toInt()),
            clay = Cost(ore = match.destructured.component3().toInt()),
            obsidian = Cost(
                ore = match.destructured.component4().toInt(),
                clay = match.destructured.component5().toInt()
            ),
            geode = Cost(
                ore = match.destructured.component6().toInt(),
                obsidian = match.destructured.component7().toInt()
            )
        )
    }
    val levels = mutableListOf<Int>()
    blueprints.forEach { blueprint, robotCosts ->
        println("blueprint=$blueprint $robotCosts")
        var states = listOf(State.initial())
        for (minute in 1..24) {
            //println("== Minute $minute ==")
            states = states.map { state ->
                state.whichRobotsCanBeBuild(robotCosts).map { state.collect().buildRobot(it, robotCosts.forType(it)) }
                    .plus(state.collect())
            }.flatten()
            //reduce number of states
            val maxGeodeRobots = states.maxOfOrNull { s -> s.geodeRobotsCount } ?: 0
            states = states.filter { it.geodeRobotsCount == maxGeodeRobots }
        }
        val maxGeodes = states.maxByOrNull { it.geode }?.geode ?: 0
        println(maxGeodes)
        levels.add(blueprint * maxGeodes)
    }
    println(levels.sum())
}

data class State(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int,
    val oreRobotsCount: Int,
    val clayRobotsCount: Int,
    val obsidianRobotsCount: Int,
    val geodeRobotsCount: Int
) {
    fun collect(): State {
        return copy(
            ore = ore + oreRobotsCount,
            clay = clay + clayRobotsCount,
            obsidian = obsidian + obsidianRobotsCount,
            geode = geode + geodeRobotsCount
        )
    }

    fun whichRobotsCanBeBuild(costs: RobotCosts): List<RobotType> {
        return RobotType.values().mapNotNull { type ->
            val cost = costs.forType(type)
            if (ore >= cost.ore && clay >= cost.clay && obsidian >= cost.obsidian) {
                type
            } else null
        }
    }

    fun buildRobot(type: RobotType, cost: Cost): State {
        // println("building robot for $type")
        return when (type) {
            RobotType.ORE -> copy(
                ore = ore - cost.ore,
                clay = clay - cost.clay,
                obsidian = obsidian - cost.obsidian,
                oreRobotsCount = oreRobotsCount + 1
            )

            RobotType.CLAY -> copy(
                ore = ore - cost.ore,
                clay = clay - cost.clay,
                obsidian = obsidian - cost.obsidian,
                clayRobotsCount = clayRobotsCount + 1
            )

            RobotType.OBSIDIAN -> copy(
                ore = ore - cost.ore,
                clay = clay - cost.clay,
                obsidian = obsidian - cost.obsidian,
                obsidianRobotsCount = obsidianRobotsCount + 1
            )

            RobotType.GEODE -> copy(
                ore = ore - cost.ore,
                clay = clay - cost.clay,
                obsidian = obsidian - cost.obsidian,
                geodeRobotsCount = geodeRobotsCount + 1
            )
        }
    }

    companion object {
        fun initial(): State {
            return State(
                ore = 0,
                clay = 0,
                obsidian = 0,
                geode = 0,
                oreRobotsCount = 1,
                clayRobotsCount = 0,
                obsidianRobotsCount = 0,
                geodeRobotsCount = 0
            )
        }
    }
}

enum class RobotType {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

data class RobotCosts(
    val ore: Cost,
    val clay: Cost,
    val obsidian: Cost,
    val geode: Cost
) {
    fun forType(type: RobotType): Cost {
        return when (type) {
            RobotType.ORE -> ore
            RobotType.CLAY -> clay
            RobotType.OBSIDIAN -> obsidian
            RobotType.GEODE -> geode
        }
    }
}

data class Cost(
    val ore: Int,
    val clay: Int = 0,
    val obsidian: Int = 0
)