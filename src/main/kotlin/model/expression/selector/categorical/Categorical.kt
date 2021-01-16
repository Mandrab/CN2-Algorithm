package model.expression.selector.categorical

import model.expression.selector.Selector

/**
 * Represents a categorical selector
 *
 * @author Paolo Baldini
 */
data class Categorical(
    override val attribute: String,
    val value: String,
    val operator: Operator
): Selector {
    companion object {

        /**
         * Builder for construct all possible categorical selectors
         *
         * @param attribute of the value
         * @param value instance of the attribute
         * @return the possible selectors that use the two inputs
         */
        fun get(attribute: String, value: String): Set<Categorical> =
            Operator.values().map { Categorical(attribute, value, it) }.toSet()
    }

    /**
     * Test an element to check if it is covered by the selector
     *
     * @param element to test
     * @return true if the element is covered by the selector
     */
    override fun test(element: Any?): Boolean = element != null && element is String && operator.test(this, value)

    /**
     * Check if the selector is coherent with this one
     *
     * @param selector to which check contradiction
     * @return true if the two selectors does not contradict each other
     */
    override fun coherent(selector: Selector) = selector is Categorical && selector.attribute == attribute && (
                    // s1: == "x", s2: == "y" -> incoherent
                    (selector.operator == operator && operator == Operator.Equal && selector.value == value)
                    // s1: != "x", s2: != anything -> always coherent
                ||  (selector.operator == operator && operator == Operator.Different)
                    // s1: == "x", s2: != "x" -> incoherent
                ||  (selector.value != value)
            )

    /**
     * Get human readable form of the data
     *
     * @return string representing the object
     */
    override fun toString(): String = "$attribute " + when(operator) {
            Operator.Equal -> "=="
            Operator.Different -> "!="
        } + " $value"
}
