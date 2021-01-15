package model.expression.complex.selector.numerical

import model.expression.complex.selector.Selector

data class Numerical(
    override val attribute: String,
    val value: Double,
    val operator: Operators
): Selector {
    companion object {
        fun get(attribute: String, value: Double): Set<Numerical> =
            Operators.values().map { Numerical(attribute, value, it) }.toSet()
    }

    override fun test(element: Any?): Boolean = element != null && when (element) {
        is Int -> operator.test(this, element.toDouble())
        is Float -> operator.test(this, element.toDouble())
        is Double -> operator.test(this, element)
        else -> false
    }

    /**
     * Check the selector contradict this one
     *
     * @param selector to which check  contradiction
     * @return true if the two selectors contradict each other
     */
    override fun coherent(selector: Selector) = selector is Numerical && selector.attribute == attribute
            && when(selector.operator) {
                Operators.Equal -> when(operator) {
                    Operators.Equal -> selector.value == value          // s1: == 5, s2: == 6 -> incoherent
                    Operators.Different -> selector.value != value      // s1: == 5, s2: != 5 -> incoherent
                    Operators.LesserEqual -> selector.value <= value    // s1: == 5, s2: <= 4 -> incoherent
                    else -> selector.value > value                      // s1: == 5, s2: > 5 -> incoherent
                }
                Operators.Different -> when(operator) {
                    Operators.Equal -> selector.value != value          // s1: != 5, s2: == 5 -> incoherent
                    else -> true                                        // s1: != 5, s2: <=, >, != x -> always coherent
                }
                Operators.LesserEqual -> when(operator) {
                    Operators.Equal -> selector.value >= value          // s1: <= 5, s2: == 6 -> incoherent
                    Operators.Greater -> selector.value > value         // s1: <= 5, s2: > 5 -> incoherent
                    else -> true                                        // s1: <= 5, s2: <=, != x -> always coherent
                }
                Operators.Greater -> when(operator) {
                    Operators.Equal -> selector.value < value           // s1: > 5, s2: == 5 -> incoherent
                    Operators.LesserEqual -> selector.value > value     // s1: > 5, s2: <= 5 -> incoherent
                    else -> true                                        // s1: > 5, s2: >, != x -> always coherent
                }
            }

    override fun simplify(selector: Selector): Iterable<Selector> = if (selector !is Numerical) emptyList()
            else Numericals.simplify(this, selector)

    override fun toString(): String = "$attribute " + when(operator) {
        Operators.Equal -> "=="
        Operators.Different -> "!="
        Operators.Greater -> ">"
        Operators.LesserEqual -> "<="
    } + " $value"
}
