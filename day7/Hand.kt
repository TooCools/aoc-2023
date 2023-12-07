package day7

val CARDS_NORMAL = listOf("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A")
val CARDS_JOKER = listOf("J", "2", "3", "4", "5", "6", "7", "8", "9", "T", "Q", "K", "A")
val CARDS = if (JOKER_ENABLED) CARDS_JOKER else CARDS_NORMAL

data class Hand(
    val cards: String,
    val bid: Int,
    val handType: HandType = calculateHandType(cards)
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val handTypeComparison = handType.value.compareTo(other.handType.value)
        if (handTypeComparison != 0) {
            return handTypeComparison
        }
        var index = -1
        do {
            index++
            val cardComparison = compareCards(index, other)
        } while (cardComparison == 0)
        return compareCards(index, other)
    }

    private fun compareCards(index: Int, other: Hand) = CARDS.indexOf(cards[index].toString())
        .compareTo(CARDS.indexOf(other.cards[index].toString()))


}

enum class HandType(val value: Int) {
    FIVE_OF_A_KIND(6), FOUR_OF_A_KIND(5), FULL_HOUSE(4), THREE_OF_A_KIND(3), TWO_PAIR(2), ONE_PAIR(1), HIGH_CARD(0)
}

private fun calculateHandType(cards: String): HandType {
    val cardToCount = mutableMapOf<String, Int>()
    cards.split("").filter(String::isNotBlank).forEach { card ->
        cardToCount.merge(card, 1) { acc, value -> acc + value }
    }

    val handType = when (cardToCount.size) {
        1 -> HandType.FIVE_OF_A_KIND
        //must be four of kind/full house 4 -> 5, 3 -> 4
        2 -> if (cardToCount.maxOf { (_, count) -> count } == 4) HandType.FOUR_OF_A_KIND else HandType.FULL_HOUSE
        //must be three of a kind, two pair
        3 -> if (cardToCount.maxOf { (_, count) -> count } == 3) HandType.THREE_OF_A_KIND else HandType.TWO_PAIR
        //must be one pair
        4 -> HandType.ONE_PAIR
        else -> HandType.HIGH_CARD
    }
    if (!JOKER_ENABLED) {
        return handType
    }

    val jokerCount = cardToCount.getOrDefault("J", 0)
    //depending on the amount of jokers, the handType can be "upgraded"
    return when (handType) {
        HandType.FIVE_OF_A_KIND -> HandType.FIVE_OF_A_KIND
        HandType.FOUR_OF_A_KIND -> if (jokerCount == 1 || jokerCount == 4) HandType.FIVE_OF_A_KIND else HandType.FOUR_OF_A_KIND
        HandType.FULL_HOUSE -> if (jokerCount == 3 || jokerCount == 2) HandType.FIVE_OF_A_KIND else HandType.FULL_HOUSE
        HandType.THREE_OF_A_KIND -> if (jokerCount == 3 || jokerCount == 1) HandType.FOUR_OF_A_KIND else HandType.THREE_OF_A_KIND
        HandType.TWO_PAIR -> if (jokerCount == 1) HandType.FULL_HOUSE else if (jokerCount == 2) HandType.FOUR_OF_A_KIND else HandType.TWO_PAIR
        HandType.ONE_PAIR -> if (jokerCount == 2 || jokerCount == 1) HandType.THREE_OF_A_KIND else HandType.ONE_PAIR
        HandType.HIGH_CARD -> if (jokerCount == 1) HandType.ONE_PAIR else HandType.HIGH_CARD
    }
}



