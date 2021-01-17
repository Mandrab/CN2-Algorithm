package model

import model.expression.complex.Complex

/**
 * Class that represents a rule of the decision-list
 *
 * @author Paolo Baldini
 */
data class Rule(
    val complex: Complex,
    val resultingClass: Any
)
