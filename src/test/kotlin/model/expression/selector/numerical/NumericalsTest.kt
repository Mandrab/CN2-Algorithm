package model.expression.selector.numerical

import org.junit.Test
import kotlin.test.assertFails

class NumericalsTest {

    @Test
    fun simplifyEquals() {
        val a = Numerical("a", 1.0, Operator.Equal)
        val b = Numerical("a", 1.0, Operator.Equal)
        assert(Numericals.simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyDifferentAttributes() {
        val a = Numerical("a", 1.0, Operator.Equal)
        val b = Numerical("b", 1.0, Operator.Equal)
        assertFails { Numericals.simplify(a, b) }
    }

    @Test
    fun simplifyEEOperators() {
        val a = Numerical("a", 1.0, Operator.Equal)
        val b = Numerical("a", 1.0, Operator.Equal)
        assert(Numericals.simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyEDOperators() {
        val a = Numerical("a", 1.0, Operator.Equal)
        val b = Numerical("a", 1.0, Operator.Different)
        assert(Numericals.simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyELEOperators() {
        val a = Numerical("a", 1.0, Operator.Equal)
        val b = Numerical("a", 0.0, Operator.LesserEqual)
        assert(Numericals.simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyEGOperators() {
        val a = Numerical("a", 1.0, Operator.Equal)
        val b = Numerical("a", 2.0, Operator.Greater)
        assert(Numericals.simplify(a, b).firstOrNull() == a)
    }

    @Test
    fun simplifyDEOperators() {
        val a = Numerical("a", 1.0, Operator.Different)
        val b = Numerical("a", 1.0, Operator.Equal)
        assert(Numericals.simplify(a, b).firstOrNull() == b)
    }

    @Test
    fun simplifyDDOperators() {
        val a = Numerical("a", 1.0, Operator.Different)
        val b = Numerical("a", 2.0, Operator.Different)
        assert(Numericals.simplify(a, b) == listOf(a, b))
    }

    @Test
    fun simplifyDLEOperators() {
        val a = Numerical("a", 1.0, Operator.Different)
        val b = Numerical("a", 2.0, Operator.LesserEqual)
        assert(Numericals.simplify(a, b) == listOf(a, b))
    }

    @Test
    fun simplifyDLE2Operators() {
        val a = Numerical("a", 1.0, Operator.Different)
        val b = Numerical("a", 0.0, Operator.LesserEqual)
        assert(Numericals.simplify(a, b).firstOrNull() == b)
    }

    @Test
    fun simplifyDGOperators() {
        val a = Numerical("a", 1.0, Operator.Different)
        val b = Numerical("a", 2.0, Operator.Greater)
        assert(Numericals.simplify(a, b).firstOrNull() == b)
    }

    @Test
    fun simplifyDG2Operators() {
        val a = Numerical("a", 1.0, Operator.Different)
        val b = Numerical("a", 0.0, Operator.Greater)
        assert(Numericals.simplify(a, b) == listOf(a, b))
    }
}
