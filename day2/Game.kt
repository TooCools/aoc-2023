package day2

data class Game(val id: Int, val allDrawings: List<List<Pick>>) {
    override fun toString(): String {
        val drawings = allDrawings.joinToString(separator = "; ")
        { drawings -> drawings.joinToString(separator = ", ") { drawing -> drawing.toString() } }
        return "Game ${id}: $drawings"
    }

    fun isPossible(fullDrawing: List<Pick>) = allDrawings.all { picks -> picksMatch(picks, fullDrawing) }

    private fun picksMatch(picks: List<Pick>, fullDrawing: List<Pick>) =
        picks.all { pick -> matchingFullDrawingPick(fullDrawing, pick).number >= pick.number }


    private fun matchingFullDrawingPick(fullDrawing: List<Pick>, pick: Pick) =
        fullDrawing.first { fullPick -> fullPick.matches(pick) }

    fun minimalPicks(): List<Pick> {
        val colors = allDrawings.flatMap { picks -> picks.map { pick -> pick.color } }.distinct()
        return  colors.map { color -> Pick(color,  allDrawings.flatMap { picks -> picks.filter { pick -> pick.color == color } }.maxOf { pick->pick.number }) }
    }
}

data class Pick(val color: String, val number: Int) {

    fun matches(other: Pick) = this.color == other.color

    override fun toString(): String {
        return "$number $color"
    }
}