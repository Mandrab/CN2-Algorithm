package model.expression.complex.selector.numerical

object Numericals {

    /*fun canSimplify(old: Numerical, new: Numerical) = old.attribute == new.attribute && (
            (old.operator == Operators.Equal || new.operator == Operators.Equal)
            )*/

    /**
     * Simplify the set of selectors keeping only useful ones
     *
     * @param selectors the set to simplify
     * @return the simplified set of selectors
     */
    fun simplify(vararg selectors: Numerical): Iterable<Numerical> {
        assert(selectors.all { it.attribute == selectors.first().attribute })

        // equal selector makes all the others useless
        selectors.firstOrNull { it.operator == Operators.Equal } ?.let {
            return listOf(it)
        }

        // discretize between same operators type
        val reducedSelectors = selectors.groupBy { it.operator }.mapValues { when (it.key) {
            Operators.Greater -> listOf(it.value.maxByOrNull { it.value }!!)    // take greater with highest value
            Operators.LesserEqual -> listOf(it.value.minByOrNull { it.value }!!)// take lower with lower value
            else -> it.value.distinct()                                         // take distinct 'different' selectors
        } }

        // remove different selectors out of ranges
        return reducedSelectors.map { when (it.key) {
            Operators.Different -> it.value.filter { selector ->
                reducedSelectors[Operators.LesserEqual]?.all { it.value > selector.value } ?: true &&
                reducedSelectors[Operators.Greater]?.all { it.value < selector.value } ?: true
            }
            else -> it.value
        } }.fold(emptyList()) { acc, list -> acc + list }
    }

    /*fun simplify(old: Numerical, new: Numerical): Iterable<Numerical> {

        return when {
            old.attribute != new.attribute -> listOf(old, new)
            old == new -> listOf(old)
            old.operator == Operators.Equal -> listOf(old)
            new.operator == Operators.Equal -> listOf(new)
            old.operator == Operators.Different -> when (new.operator) {
                Operators.Different -> listOf(old, new)
                Operators.Greater -> if (old.value > new.value) listOf(old, new) else listOf(new)
                else -> if (old.value <= new.value) listOf(old, new) else listOf(new)
            }
            old.operator == Operators.LesserEqual -> when (new.operator) {
                Operators.Different -> if (old.value >= new.value) listOf(old, new) else listOf(old)
                Operators.LesserEqual -> if (old.value < new.value) listOf(old) else listOf(new)
                else -> listOf(old, new) // selectors should be already coherent
            }
            else -> when (new.operator) {
                Operators.Different -> if (old.value < new.value) listOf(old, new) else listOf(old)
                Operators.LesserEqual -> listOf(old, new) // selectors should be already coherent
                else -> if (old.value > new.value) listOf(old) else listOf(new)
            }
        }
    }*/
}
