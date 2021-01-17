package model.expression.selector.categorical

import model.expression.selector.categorical.Categoricals.simplify
import org.junit.Test
import kotlin.test.assertFails

class CategoricalsTest {

    @Test
    fun simplifyEquals() {
        val a = Categorical("a", "x", Operator.Equal)
        val b = Categorical("a", "x", Operator.Equal)
        assert(simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyDifferentAttributes() {
        val a = Categorical("a", "x", Operator.Equal)
        val b = Categorical("b", "x", Operator.Equal)
        assertFails { simplify(a, b) }
    }

    @Test
    fun simplifyDifferentOperator() {
        val a = Categorical("a", "x", Operator.Equal)
        val b = Categorical("a", "x", Operator.Different)
        assert(simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyDifferentOperator2() {
        val a = Categorical("a", "x", Operator.Different)
        val b = Categorical("a", "x", Operator.Equal)
        assert(simplify(a, b).firstOrNull() == b)
    }

    @Test
    fun simplify2xDifferentOperator() {
        val a = Categorical("a", "x", Operator.Different)
        val b = Categorical("a", "c", Operator.Different)
        assert(simplify(a, b) == listOf(a, b))
    }
}
