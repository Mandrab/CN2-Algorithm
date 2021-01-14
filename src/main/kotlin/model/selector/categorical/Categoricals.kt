package model.selector.categorical

object Categoricals {

    /**
     * Analyze two categorical selector and decide if they are coherent.
     * Cases:
     *  - Different attributes: coherent
     *  - 2x 'Different': coherent
     *  - 2x 'Equal': incoherent unless same value
     *  - Other: coherent unless same value
     *
     * @return true if the two categorical are a coherent pair
     */
    fun coherents(old: Categorical, new: Categorical) = old.attribute != new.attribute
            || (old.operator == Operators.Different && new.operator == Operators.Different)
            || (old.operator != new.operator && old.value != new.value)
            || (old.value == new.value)

    fun canSimplify(old: Categorical, new: Categorical) = old.attribute == new.attribute && coherents(old, new)
            && ! (old.operator == new.operator && old.operator == Operators.Different && old.value != new.value)

    fun simplify(old: Categorical, new: Categorical): Iterable<Categorical> {
        assert(coherents(old, new))

        return when {
            old.attribute != new.attribute -> listOf(old, new)  // different attributes
            old == new -> listOf(old)                           // same selector
            old.operator == Operators.Equal -> listOf(old)      // the two are coherent so return the first
            new.operator == Operators.Different -> listOf(old, new) // two 'different'
            else -> listOf(new)                                 // the two are coherent so return the second
        }
    }
}
