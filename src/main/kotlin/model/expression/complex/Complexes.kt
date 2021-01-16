package model.expression.complex

import com.google.common.collect.Comparators
import krangl.DataFrame
import model.expression.selector.Selector
import kotlin.Comparator
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.sign

const val Delta = 0.00001

/**
 * Utilities to work with complexes
 *
 * @author Paolo Baldini
 */
object Complexes {

    /**
     * Return a function to evaluate the complex quality in the coverage of the dataframe
     *
     * @param data set of the examples that the complex should cover
     * @return A function that take the complex to evaluate and returns a pair whose values are:
     *  - an int representing the covered examples
     *  - a double representing the coverage. The lower it is, the better it is
     */
    fun evaluate(data: DataFrame): (Complex) -> Pair<Int, Double> = { complex ->
            val coveredRows = data.filterByRow { complex.cover(it) }        // takes only rows covered by the complex
            val coveredRowsCount = coveredRows.nrow                         // count examples covered
            coveredRowsCount to if (coveredRowsCount == 0) Double.POSITIVE_INFINITY
            else - coveredRows.rows.groupBy { it[data.names.first()] }      // group by class (first column)
                .map { it.value.count() / coveredRowsCount.toDouble() }     // to probability distribution
                .map { it * log2(it) }.sum()                                // calculate entropy
        }

    /**
     * Find the best complex specializing the star-set
     *
     * @param starSetSize required size of the star-set
     * @param selectors to use for the specialization
     * @param evaluate strategy for the complexes evaluation
     * @return best complex or null if there isn't any available complex
     */
    fun bestComplex(starSetSize: Int, selectors: Set<Selector>, evaluate: (Complex) -> Pair<Int, Double>): Complex? {

        // utility class to evaluate the complex
        class EvaluatedComplex(vararg selectors: Selector): Complex(selectors.toSet()) {
            constructor(complex: Complex): this(*complex.selectors.toTypedArray())
            val score = evaluate(this)
        }
        // function to compare/sort complexes
        val comparator: Comparator<EvaluatedComplex> = Comparator { f, s -> comparator.compare(f.score, s.score) }

        // initial set of the best selectors
        var starSet = selectors.stream().map{ EvaluatedComplex(it) }.collect(Comparators.least(starSetSize, comparator))
        var bestComplex = starSet.firstOrNull() // best selector/complex so far

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
                .parallelStream() // TODO check if it effectively find the best // parallel execution for performance
                .filter { ! it.isNull() }                                       // remove incoherent (null) complexes
                .map { it.simplify() }                                          // simplify the complex's selectors set
                .filter { ! starSet.contains(it) }                              // remove complexes present in star
                .map { EvaluatedComplex(it) }                                   // evaluate the complex quality
                .collect(Comparators.least(starSetSize, comparator))            // get first starSetSize best complexes
        } while (starSet.isNotEmpty())
        return bestComplex
    }

    /** Comparator for results of the evaluation function */
    val comparator: Comparator<Pair<Int, Double>> = Comparator { f, s ->
        val difference = f.second - s.second // difference between quality of coverage

        // if almost same quality of coverage consider number of covered examples (invert sign)
        if (abs(difference) < Delta) s.first - f.first else sign(difference).toInt()
    }
}
