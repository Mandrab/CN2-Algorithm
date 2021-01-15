package model.expression.selector.categorical

import model.expression.selector.categorical.Categoricals.simplify
import org.junit.Test

class CategoricalsTest {

    @Test
    fun simplifyEquals() {
        val a = Categorical("a", "x", Operators.Equal)
        val b = Categorical("a", "x", Operators.Equal)
        assert(simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyDifferentAttributes() {
        val a = Categorical("a", "x", Operators.Equal)
        val b = Categorical("b", "x", Operators.Equal)
        assert(simplify(a, b) == listOf(a, b))
    }

    @Test
    fun simplifyDifferentOperator() {
        val a = Categorical("a", "x", Operators.Equal)
        val b = Categorical("a", "x", Operators.Different)
        assert(simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyDifferentOperator2() {
        val a = Categorical("a", "x", Operators.Different)
        val b = Categorical("a", "x", Operators.Equal)
        assert(simplify(a, b).firstOrNull() == b)
    }

    @Test
    fun simplify2xDifferentOperator() {
        val a = Categorical("a", "x", Operators.Different)
        val b = Categorical("a", "c", Operators.Different)
        assert(simplify(a, b) == listOf(a, b))
    }
}