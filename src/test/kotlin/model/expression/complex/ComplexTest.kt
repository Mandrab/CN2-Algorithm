package model.expression.complex

import model.expression.selector.categorical.Categorical
import model.expression.selector.categorical.Operators as COperators
import model.expression.selector.numerical.Numerical
import model.expression.selector.numerical.Operators as NOperators
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class ComplexTest {

    @Test
    fun covering() {
        val row = mapOf("a" to 1, "b" to "1")
        val complex = Complex(
            Numerical("a", 2.0, NOperators.LesserEqual),
            Categorical("b", "1", COperators.Equal)
        )
        assert(complex.cover(row))
    }

    @Test
    fun notCovering() {
        val row = mapOf("a" to 1, "b" to "2")
        val complex = Complex(
            Numerical("a", 2.0, NOperators.LesserEqual),
            Categorical("b", "2", COperators.Equal)
        )
        assert(complex.cover(row))
    }

    @Test
    fun dummySpecialize() {
        val selector = Numerical("a", 2.0, NOperators.LesserEqual)
        val complex = Complex(selector, Categorical("b", "1", COperators.Equal))
        assertEquals(complex.specialize(selector), complex)
    }

    @Test
    fun specialize() {
        val selector = Numerical("a", 2.0, NOperators.LesserEqual)
        val complex = Complex(Categorical("b", "1", COperators.Equal))
        assertNotEquals(complex.specialize(selector), complex)
    }

    @Test
    fun isNull() {
        val complex = Complex(
            Categorical("b", "1", COperators.Different),
            Categorical("b", "1", COperators.Equal)
        )
        assert(complex.isNull())
    }

    @Test
    fun isNotNull() {
        val complex = Complex(
            Categorical("b", "2", COperators.Different),
            Categorical("b", "1", COperators.Equal)
        )
        assertFalse(complex.isNull())
    }

    @Test
    fun objectEquals() {
        val complex = Complex(
            Categorical("b", "1", COperators.Different),
            Categorical("b", "1", COperators.Equal)
        )
        assert(complex == complex)
    }

    @Test
    fun equals() {
        val complex1 = Complex(
            Categorical("b", "1", COperators.Different),
            Categorical("b", "1", COperators.Equal)
        )
        val complex2 = Complex(
            Categorical("b", "1", COperators.Different),
            Categorical("b", "1", COperators.Equal)
        )
        assert(complex1 == complex2)
    }

    @Test
    fun notEquals() {
        val complex1 = Complex(
            Categorical("b", "1", COperators.Equal)
        )
        val complex2 = Complex(
            Categorical("b", "1", COperators.Different)
        )
        assert(complex1 != complex2)
    }

    @Test
    fun simplify() {
        val complex = Complex(
            Numerical("a", 1.0, NOperators.Equal),
            Numerical("a", 2.0, NOperators.Different)
        )
        val expectedResult = Complex(Numerical("a", 1.0, NOperators.Equal))
        assert(complex.simplify() == expectedResult)
    }

    @Test
    fun simplifyNothing() {
        val complex = Complex(
            Numerical("a", 1.0, NOperators.Equal),
            Numerical("b", 2.0, NOperators.Different)
        )
        assert(complex.simplify() == complex)
    }
}