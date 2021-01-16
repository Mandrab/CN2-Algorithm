package model.expression.selector.numerical

/**
 * Utilities to work on Numerical
 *
 * @author Paolo Baldini
 */
object Numericals {

    /**
     * Simplify the set of selectors keeping only useful ones
     *
     * @param selectors the set to simplify
     * @return the simplified set of selectors
     */
    fun simplify(vararg selectors: Numerical): Iterable<Numerical> {
        assert(selectors.all { it.attribute == selectors.first().attribute })

        // equal selector makes all the others useless
        selectors.firstOrNull { it.operator == Operator.Equal } ?.let {
            return listOf(it)
        }

        // discretize between same operators type
        val reducedSelectors = selectors.groupBy { it.operator }.mapValues { when (it.key) {
            Operator.Greater -> listOf(it.value.maxByOrNull { it.value }!!)     // take greater with highest value
            Operator.LesserEqual -> listOf(it.value.minByOrNull { it.value }!!) // take lower with lower value
            else -> it.value.distinct()                                         // take distinct 'different' selectors
        } }

        // remove different selectors out of ranges
        return reducedSelectors.map { when (it.key) {
            Operator.Different -> it.value.filter { selector ->
                reducedSelectors[Operator.LesserEqual]?.all { it.value > selector.value } ?: true &&
                reducedSelectors[Operator.Greater]?.all { it.value < selector.value } ?: true
            }
            else -> it.value
        } }.fold(emptyList()) { acc, list -> acc + list }
    }
}
