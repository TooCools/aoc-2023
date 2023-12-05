class NeighbourUtil {
    companion object {
        fun generateNeighbours(
            rowIndex: Int,
            columnIndex: Int,
            maxRowIndex: Int,
            maxColumnIndex: Int,
            includeDiagonal: Boolean = true
        ): List<Pair<Int, Int>> {
            val neighbours = ArrayList<Pair<Int, Int>>()
            val rowGreaterThanZero = rowIndex > 0
            val columnGreaterThanZero = columnIndex > 0
            val rowSmallerThanMax = rowIndex < maxRowIndex
            val columnSmallerThanMax = columnIndex < maxColumnIndex

            if (rowGreaterThanZero) {
                neighbours.add(Pair(rowIndex - 1, columnIndex))
            }
            if (rowSmallerThanMax) {
                neighbours.add(Pair(rowIndex + 1, columnIndex))
            }
            if (columnGreaterThanZero) {
                neighbours.add(Pair(rowIndex, columnIndex - 1))
            }
            if (columnSmallerThanMax) {
                neighbours.add(Pair(rowIndex, columnIndex + 1))
            }
            if (includeDiagonal) {
                if (rowGreaterThanZero && columnGreaterThanZero) {
                    neighbours.add(Pair(rowIndex - 1, columnIndex - 1))
                }
                if (rowGreaterThanZero && columnSmallerThanMax) {
                    neighbours.add(Pair(rowIndex - 1, columnIndex + 1))
                }
                if (rowSmallerThanMax && columnGreaterThanZero) {
                    neighbours.add(Pair(rowIndex + 1, columnIndex - 1))
                }
                if (rowSmallerThanMax && columnSmallerThanMax) {
                    neighbours.add(Pair(rowIndex + 1, columnIndex + 1))
                }
            }
            return neighbours
        }
    }


}