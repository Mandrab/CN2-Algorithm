package model.expression.complex

import com.google.common.collect.Comparators
import krangl.DataFrame
import model.expression.selector.Selector
import kotlin.Comparator
import kotlin.math.*

private typealias Distribution = Map<Any, Double>
private const val Delta = 0.00001

/**
 * Utilities to work with complexes
 *
 * @author Paolo Baldini
 */
object Complexes {

    /**
     * Evaluate a complex through use of a function
     *
     * @param evaluationFunction function for evaluate the complex
     * @return a function who takes the covered examples and the complex to evaluate
     */
    fun <T> evaluate(evaluationFunction: (Distribution) -> T) = { data: DataFrame -> { complex: Complex ->
        val coveredExamples = data.filterByRow { complex.cover(it) }    // takes only rows covered by the complex
        coveredExamples.nrow to
                evaluationFunction(classDistribution(coveredExamples))  // evaluate class distribution of examples
    } }

    /** Calculate entropy based on class distribution in covered examples */
    val entropy = { distribution: Map<Any, Double> ->
        distribution.map { -it.value * log2(it.value) }.ifEmpty { listOf(Double.POSITIVE_INFINITY) }.sum()
    }

    /** Calculate significance based on class distribution in data and covered examples */
    val significance = { completeDistribution: Map<Any, Double> -> { coveredDistribution: Map<Any, Double> ->
        2.0 * coveredDistribution.map { it.value * log10(it.value / completeDistribution[it.key]!!) }.sum()
    } }

    /**
     * Find the best complex specializing the star-set
     *
     * @param starSetSize required size of the star-set
     * @param selectors to use for the specialization
     * @param entropy strategy to calculate complex entropy
     * @param significance strategy to calculate complex significance
     * @return best complex or null if there isn't any available complex
     */
    fun bestComplex(selectors: Set<Selector>, starSetSize: Int, threshold: Double,
                    entropy: (Complex) -> Pair<Int, Double>, significance: (Complex) -> Pair<Int, Double>): Complex? {

        // utility class to evaluate the complex
        class EvaluatedComplex(vararg selectors: Selector): Complex(selectors.toSet()) {
            constructor(complex: Complex): this(*complex.selectors.toTypedArray())
            val score = entropy(this)
            var significance = lazy { significance(this).second }
        }
        // function to compare/sort complexes
        val comparator: Comparator<EvaluatedComplex> = Comparator { f, s -> comparator.compare(f.score, s.score) }

        // initial set of the best selectors
        var starSet = selectors.stream().map{ EvaluatedComplex(it) }.collect(Comparators.least(starSetSize, comparator))
        var bestComplex = starSet.firstOrNull()                         // best selector/complex so far

        // iterate till the star-set has no more items
        do {
            starSet.firstOrNull()?.let { challenger ->                  // take the best complex of the set (if present)
                bestComplex?.let {                                      // a temporary best complex already exists
                    if (comparator.compare(bestComplex, challenger) > 0)// if challenger better than actual
                        bestComplex = challenger                        // set challenger as best complex
                } ?: let {                                              // no temporary best complex exists
                    bestComplex = challenger                            // set challenger as best complex
                }
            }

            starSet = starSet
                .flatMap { complex -> selectors.map { complex.specialize(it) } }// specialize all the complexes
                .parallelStream()                                       // parallel execution for performance
                .filter { ! it.isNull() }                               // remove incoherent (null) complexes
                .map { it.simplify() }                                  // simplify the complex's selectors set
                .filter { ! starSet.contains(it) }                      // remove complexes present in star
                .map { EvaluatedComplex(it) }                           // evaluate the complex quality
                .filter { it.significance.value > threshold }           // filter non-significant values
                .collect(Comparators.least(starSetSize, comparator))    // get first starSetSize best complexes
        } while (starSet.isNotEmpty())
        return bestComplex
    }

    /**
     * Calculate class distribution in the dataset
     *
     * @param data set of example with which calculate the probabilities
     * @return probability distribution for each class
     */
    fun classDistribution(data: DataFrame) = data.rows
        .groupBy { it[data.names.first()]!! }                           // group by class (first column)
        .mapValues { it.value.count() / data.nrow.toDouble() }          // to probability distribution

    /** Comparator for results of the evaluation function */
    private val comparator: Comparator<Pair<Int, Double>> = Comparator { f, s ->
        val difference = f.second - s.second                            // difference between quality of coverage

        // if almost same quality of coverage consider number of covered examples (invert sign)
        if (abs(difference) < Delta) s.first - f.first else sign(difference).toInt()
    }
}
