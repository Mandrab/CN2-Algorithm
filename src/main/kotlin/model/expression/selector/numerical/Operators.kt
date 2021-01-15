package model.expression.selector.numerical

import kotlin.math.abs

const val Delta = 0.00001

enum class Operators(val test: (selector: Numerical, value: Double) -> Boolean) {
    Equal(test = { s, v -> abs(s.value - v) < Delta }),
    Different(test = { s, v -> abs(s.value - v) > Delta }),
    Greater(test = { s, v -> s.value - Delta < v }),
    LesserEqual(test = { s, v -> s.value + Delta >= v })
}
