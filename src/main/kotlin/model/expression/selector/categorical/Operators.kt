package model.expression.selector.categorical

enum class Operators(val test: (selector: Categorical, value: String) -> Boolean) {
    Equal(test = { s, v -> s.value == v }),
    Different(test = { s, v -> s.value != v })
}
