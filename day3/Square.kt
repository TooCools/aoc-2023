package day3

sealed class Square(val rowIndex: Int, val columnIndex: Int) : Comparable<Square> {
    fun coordinates() = Pair(rowIndex, columnIndex)
    override fun compareTo(other: Square): Int {
        return if (this.rowIndex != other.rowIndex) {
            compareValues(this.rowIndex, other.rowIndex)
        } else {
            compareValues(this.columnIndex, other.columnIndex)
        }
    }


}

data class IntSquare(val row: Int, val column: Int, val value: Int) : Square(row, column)

data class SymbolSquare(val row: Int, val column: Int, val value: String) : Square(row, column)

data class EmptySquare(val row: Int, val column: Int) : Square(row, column)
