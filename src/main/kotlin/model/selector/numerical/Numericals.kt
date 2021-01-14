package model.selector.numerical


object Numericals {

    fun canSimplify(old: Numerical, new: Numerical) = old.attribute == new.attribute && (
            (old.operator == Operators.Equal || new.operator == Operators.Equal)
            )

    fun simplify(old: Numerical, new: Numerical): Iterable<Numerical> {

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
    }
}
