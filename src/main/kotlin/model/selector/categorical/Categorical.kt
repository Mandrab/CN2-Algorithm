package model.selector.categorical

import model.selector.Selector

class Categorical(
    override val attribute: String,
    val value: String,
    val operator: Operators
): Selector {

    override fun test(element: Any?): Boolean = element != null && element is String && operator.test(this, value)

    override fun coherent(selector: Selector) = selector is Categorical && selector.attribute == attribute
            && (
                    // s1: == "x", s2: == "y" -> incoherent
                    (selector.operator == operator && operator == Operators.Equal && selector.value == value)
                    // s1: != "x", s2: != anything -> always coherent
                ||  (selector.operator == operator && operator == Operators.Different)
                    // s1: == "x", s2: != "x" -> incoherent
                ||  (selector.value != value)
            )

    override fun toString(): String = "$attribute " + when(operator) {
            Operators.Equal -> "=="
            Operators.Different -> "!="
        } + " $value"

    companion object {

        fun get(attribute: String, value: String): Set<model.selector.categorical.Categorical> =
            Operators.values().map { Categorical(attribute, value, it) }.toSet()
    }
}
