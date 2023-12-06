package day5

data class Range(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
    fun isInRange(value: Long) = sourceRangeStart <= value && value < (sourceRangeStart + rangeLength)
    fun map(value: Long) = destinationRangeStart + value - sourceRangeStart
}