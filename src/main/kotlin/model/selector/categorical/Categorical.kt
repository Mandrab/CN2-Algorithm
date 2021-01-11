package model.selector.categorical

import model.selector.Selector

class Categorical(
    override val attribute: String,
    val value: String,
    val operator: Operators
): Selector {

    override fun test(element: Any?): Boolean = element != null && element is String && operator.test(this, value)

    override fun toString(): String = "$attribute " + when(operator) {
        Operators.Equal -> "=="
        Operators.Different -> "!="
    } + " $value"

    companion object {

        fun get(attribute: String, value: String): Set<model.selector.categorical.Categorical> =
            Operators.values().map { Categorical(attribute, value, it) }.toSet()
    }
}
