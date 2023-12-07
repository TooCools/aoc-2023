package day6

import java.util.stream.LongStream
import kotlin.streams.toList

fun main() {
    listOf(input, input2).forEach { raceInput ->
        val races = raceInput.map { raceData -> Race(raceData.key, raceData.value) }

        val maxTime = races.maxOf { race -> race.totalTime }

        val possibleBoatCombinations = LongStream.range(1, maxTime).toList()
            .map { timePressed -> Boat(timePressed) }

        val numberOfWaysToWinTheRace = races.map { race ->
            possibleBoatCombinations
                .filter { boat -> boat.timePressed < race.totalTime }
                .map { boat -> boat.calculatedDistance(race.totalTime) }
                .count { distance -> distance > race.maxDistance }
        }
        val result = numberOfWaysToWinTheRace
            .reduce { acc, value -> acc * value }

        println("Result: $result")
    }

}

data class Race(
    val totalTime: Long,
    val maxDistance: Long
)

data class Boat(val timePressed: Long) {
    fun calculatedDistance(totalTime: Long): Long {
        return timePressed * (totalTime - timePressed)
    }
}


val exampleInput = mapOf(7L to 9L, 15L to 40L, 30L to 200L)
val exampleInput2 = mapOf(71530L to 940200L)
val input = mapOf(34L to 204L, 90L to 1713L, 89L to 1210L, 86L to 1780L)
val input2 = mapOf(34908986L to 204171312101780L)