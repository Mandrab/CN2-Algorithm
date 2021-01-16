package model.expression.selector.numerical

import kotlin.math.abs

const val Delta = 0.00001   // used to check 'equality' for float and double data

/**
 * Analysis operators for numerical attributes
 *
 * @author Paolo Baldini
 */
enum class Operator(
    val test: (selector: Numerical, value: Double) -> Boolean   // allow to check if a number is covered by the operator
) {
    Equal(test = { s, v -> abs(s.value - v) < Delta }),
    Different(test = { s, v -> abs(s.value - v) > Delta }),
    Greater(test = { s, v -> s.value - Delta < v }),
    LesserEqual(test = { s, v -> s.value + Delta >= v })
}
