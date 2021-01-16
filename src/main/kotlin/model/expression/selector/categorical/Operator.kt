package model.expression.selector.categorical

/**
 * Analysis operators for categorical attributes
 *
 * @author Paolo Baldini
 */
enum class Operator(
    val test: (selector: Categorical, value: String) -> Boolean // allow to check if a string is covered by the operator
) {
    Equal(test = { s, v -> s.value == v }),
    Different(test = { s, v -> s.value != v })
}
