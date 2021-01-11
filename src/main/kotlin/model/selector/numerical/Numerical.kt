package model.selector.numerical

import model.selector.Selector

class Numerical(
    override val attribute: String,
    val value: Double,
    val operator: Operators
): Selector {

    override fun test(element: Any?): Boolean = element != null
            && (element is Int || element is Float || element is Double) && operator.test(this, value)

    override fun toString(): String = "$attribute " + when(operator) {
        Operators.Equal -> "=="
        Operators.Different -> "!="
        Operators.Greater -> ">"
        Operators.LesserEqual -> "<="
    } + " $value"

    companion object {

        fun get(attribute: String, value: Double): Set<model.selector.numerical.Numerical> =
            Operators.values().map { Numerical(attribute, value, it) }.toSet()
    }
}
