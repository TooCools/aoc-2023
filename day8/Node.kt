package day8

data class Node(val source: String, val leftDestination: String, val rightDestination: String) {
    fun getDestination(direction: String) = if (direction == "L") leftDestination else rightDestination
}