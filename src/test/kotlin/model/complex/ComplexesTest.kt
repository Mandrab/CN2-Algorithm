package model.complex

import krangl.dataFrameOf
import model.selector.numerical.Numerical
import model.selector.numerical.Operators
import org.junit.Test
import kotlin.test.fail

class ComplexesTest {

    private val df = dataFrameOf(
        "class",    "a",    "b")(
        1,          1,      1,
        1,          2,      1,
        2,          2,      2,
        3,          2,      2
    )

    @Test
    fun evaluateMax() {
        /* evaluate: - 1 * 1 * log2(1) = 0 */
        val complex = Complex(Numerical("b", 1.0, Operators.Equal))
        val score = Complexes.evaluate(df)(complex)
        assert(-0.0001 < score && score < 0.0001) { "Resulting value $score expected value ~ 0.0" }
    }

    @Test
    fun evaluateNonMax() {
        /* evaluate: - 3 * 0.3 * log2(0.3) = 1.56 */
        val complex = Complex(Numerical("a", 2.0, Operators.Equal))
        val score = Complexes.evaluate(df)(complex)
        assert(score > 1.55 && score < 1.6) { "Resulting value $score expected value ~ 1.56" }
    }

    @Test
    fun evaluateNonCovering() {
        /* evaluate: - 3 * 0 * log2(0) = 1.56 */
        val a = Complex(Numerical("a", 100.0, Operators.Equal))
        assert(Complexes.evaluate(df)(a) == Double.POSITIVE_INFINITY)
    }

    @Test
    fun evaluate() {
        /* evaluate: - 3 * 0.3 * log2(0.3) = 1.56 */
        val a = Complex(Numerical("a", 2.0, Operators.Equal))
        /* evaluate: - 1 * 1 * log2(1) = 0 */
        val b = Complex(Numerical("b", 1.0, Operators.Equal))   // this should be preferred
        assert(Complexes.evaluate(df)(a) > Complexes.evaluate(df)(b))
    }
}
