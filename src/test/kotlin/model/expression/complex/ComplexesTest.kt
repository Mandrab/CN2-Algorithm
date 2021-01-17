package model.expression.complex

import krangl.dataFrameOf
import model.expression.selector.numerical.Numerical
import model.expression.selector.numerical.Operator
import org.junit.Test

class ComplexesTest {

    private val df = dataFrameOf(
        "class",    "a",    "b")(
        1,          1,      1,
        1,          2,      1,
        2,          2,      2,
        3,          2,      2,
        4,          3,      3,
        4,          3,      5,
        4,          3,      6
    )

    @Test
    fun evaluateMax() {
        /* evaluate: - 1 * 1 * log2(1) = 0 */
        val complex = Complex(Numerical("b", 1.0, Operator.Equal))
        val score = Complexes.evaluate(df)(complex)
        assert(-0.0001 < score.second && score.second < 0.0001) { "Resulting value $score expected value ~ 0.0" }
    }

    @Test
    fun evaluateNonMax() {
        /* evaluate: - 3 * 0.3 * log2(0.3) = 1.56 */
        val complex = Complex(Numerical("a", 2.0, Operator.Equal))
        val score = Complexes.evaluate(df)(complex)
        assert(score.second > 1.55 && score.second < 1.6) { "Resulting value $score expected value ~ 1.56" }
    }

    @Test
    fun evaluateNonCovering() {
        val a = Complex(Numerical("a", 100.0, Operator.Equal))
        assert(Complexes.evaluate(df)(a).second == Double.POSITIVE_INFINITY)
    }

    @Test
    fun evaluateSameEntropy() {
        val a = Complex(Numerical("a", 3.0, Operator.Equal))
        val b = Complex(Numerical("b", 1.0, Operator.Equal))
        val scoreA = Complexes.evaluate(df)(a)
        val scoreB = Complexes.evaluate(df)(b)
        assert(scoreA.second == scoreB.second)
        assert(scoreA.first > scoreB.first)
    }

    @Test
    fun evaluate() {
        /* evaluate: - 3 * 0.3 * log2(0.3) = 1.56 */
        val a = Complex(Numerical("a", 2.0, Operator.Equal))
        /* evaluate: - 1 * 1 * log2(1) = 0 */
        val b = Complex(Numerical("b", 1.0, Operator.Equal))   // this should be preferred
        assert(Complexes.evaluate(df)(a).second > Complexes.evaluate(df)(b).second)
    }
}
