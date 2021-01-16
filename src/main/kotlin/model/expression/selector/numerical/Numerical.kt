package model.expression.selector.numerical

import model.expression.selector.Selector

/**
 * Represents a numerical selector
 *
 * @author Paolo Baldini
 */
data class Numerical(
    override val attribute: String,
    val value: Double,
    val operator: Operator
): Selector {
    companion object {

        /**
         * Builder for construct all possible numerical selectors
         *
         * @param attribute of the value
         * @param value instance of the attribute
         * @return the possible selectors that use the two inputs
         */
        fun get(attribute: String, value: Double): Set<Numerical> =
            Operator.values().map { Numerical(attribute, value, it) }.toSet()
    }

    /**
     * Test an element to check if it is covered by the selector
     *
     * @param element to test
     * @return true if the element is covered by the selector
     */
    override fun test(element: Any?): Boolean = element != null && when (element) {
        is Int -> operator.test(this, element.toDouble())
        is Float -> operator.test(this, element.toDouble())
        is Double -> operator.test(this, element)
        else -> false
    }

    /**
     * Check if the selector is coherent with this one
     *
     * @param selector to which check contradiction
     * @return true if the two selectors does not contradict each other
     */
    override fun coherent(selector: Selector) = selector is Numerical && selector.attribute == attribute
            && when(selector.operator) {
                Operator.Equal -> when(operator) {
                    Operator.Equal -> selector.value == value          // s1: == 5, s2: == 6 -> incoherent
                    Operator.Different -> selector.value != value      // s1: == 5, s2: != 5 -> incoherent
                    Operator.LesserEqual -> selector.value <= value    // s1: == 5, s2: <= 4 -> incoherent
                    else -> selector.value > value                      // s1: == 5, s2: > 5 -> incoherent
                }
                Operator.Different -> when(operator) {
                    Operator.Equal -> selector.value != value          // s1: != 5, s2: == 5 -> incoherent
                    else -> true                                        // s1: != 5, s2: <=, >, != x -> always coherent
                }
                Operator.LesserEqual -> when(operator) {
                    Operator.Equal -> selector.value >= value          // s1: <= 5, s2: == 6 -> incoherent
                    Operator.Greater -> selector.value > value         // s1: <= 5, s2: > 5 -> incoherent
                    else -> true                                        // s1: <= 5, s2: <=, != x -> always coherent
                }
                Operator.Greater -> when(operator) {
                    Operator.Equal -> selector.value < value           // s1: > 5, s2: == 5 -> incoherent
                    Operator.LesserEqual -> selector.value > value     // s1: > 5, s2: <= 5 -> incoherent
                    else -> true                                        // s1: > 5, s2: >, != x -> always coherent
                }
            }

    /**
     * Get human readable form of the data
     *
     * @return string representing the object
     */
    override fun toString(): String = "$attribute " + when(operator) {
        Operator.Equal -> "=="
        Operator.Different -> "!="
        Operator.Greater -> ">"
        Operator.LesserEqual -> "<="
    } + " $value"
}
