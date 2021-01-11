package model.selector.numerical

const val Delta = 0.00001

enum class Operators(val test: (selector: Numerical, value: Double) -> Boolean) {
    Equal(test = { s, v -> kotlin.math.abs(s.value - v) < Delta }),
    Different(test = { s, v -> kotlin.math.abs(s.value - v) > Delta }),
    Greater(test = { s, v -> s.value - v > Delta }),
    LesserEqual(test = { s, v -> s.value - v <= Delta })
}
