package model.expression.selector.categorical

/**
 * Utilities to work on Categorical
 *
 * @author Paolo Baldini
 */
object Categoricals {

    /**
     * Simplify the set of selectors keeping only useful ones
     *
     * @param selectors the set to simplify
     * @return the simplified set of selectors
     */
    fun simplify(vararg selectors: Categorical): Iterable<Categorical> {
        assert(selectors.all { it.attribute == selectors.first().attribute })

        // equal selector makes all the others useless
        selectors.firstOrNull { it.operator == Operator.Equal } ?.let {
            return listOf(it)
        }

        // return distinct 'different' selectors
        return selectors.distinct()
    }
}
