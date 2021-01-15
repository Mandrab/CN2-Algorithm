package model.complex

import krangl.DataFrame
import model.selector.Selector
import java.util.*
import kotlin.Comparator
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.sign

const val Delta = 0.00001

object Complexes {

    /**
     * Evaluate the complex quality in the coverage of the dataframe
     *
     * @param dataframe set of the examples to cover
     * @param complex to evaluate
     * @return a pair whose values are respectively:
     *  - an int representing the covered examples
     *  - a double representing the coverage. The lower it is, the better it is
     */
    fun evaluate(data: DataFrame): (Complex) -> Pair<Int, Double> = { complex ->
            /*val coveredRows = data.rows.toList().parallelStream().filter { complex.cover(it) }.toList()  // takes only rows covered by the complex
            val coveredRowsCount = coveredRows.size                         // count examples covered
            coveredRowsCount to if (coveredRowsCount == 0) Double.POSITIVE_INFINITY
            else - coveredRows.groupBy { it[data.names.first()] }           // group by class (first column)
                .map { it.value.count() / coveredRowsCount.toDouble() }     // to probability distribution
                .map { it * log2(it) }.sum()                                // calculate entropy*/
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
     * @param starSetSize required size of the output star-set
     * @param selectors to use for the specialization
     * @param evaluate strategy for the complexes evaluation
     * @return best complex or null if there isn't any available complex
     */
    fun bestComplex(starSetSize: Int, selectors: Set<Selector>, evaluate: (Complex) -> Pair<Int, Double>): Complex? {

        class EvaluatedComplex(vararg selectors: Selector): Complex(selectors.toSet()) {
            constructor(complex: Complex): this(*complex.selectors.toTypedArray())
            val score = evaluate(this)
        }
        val comparator: Comparator<EvaluatedComplex> = Comparator { f, s -> comparator.compare(f.score, s.score) }

        var newStarSet = selectors.asSequence().map { EvaluatedComplex(it) }.sortedWith(comparator).take(starSetSize)
        var bestComplex: EvaluatedComplex? = null

        // iterate till the star-set has items
        do {
            newStarSet.firstOrNull()?.let { challenger ->               // take the best complex of the set (if present)
                bestComplex?.let {                                      // a temporary best complex already exists
                    if (comparator.compare(bestComplex, challenger) > 0)// if challenger better than actual
                        bestComplex = challenger                        // set challenger as best complex
                } ?: let {                                              // no temporary best complex exists
                    bestComplex = challenger                            // set challenger as best complex
                }
            }

            val time = System.currentTimeMillis() // TODO
            newStarSet = newStarSet
                .flatMap { complex -> selectors.map { complex.specialize(it) } }// specialize all the complexes
                .filterNot { it.isNull() }                                      // remove incoherent (null) complexes
                .map { it.simplify() }                                          // simplify the complex's selectors set
                //.filterNot { newStarSet.contains(it) }                          // remove complexes present in star
                .map { EvaluatedComplex(it) }                                     // evaluate the complex quality
                .toSortedSet(comparator)                                 // order complexes by score
                .take(starSetSize)                                              // take complexes to fill the star-set
                .asSequence()
            println(System.currentTimeMillis() - time) // TODO
        } while (newStarSet.firstOrNull() != null)
        return bestComplex
    }

    val comparator: Comparator<Pair<Int, Double>> = Comparator { f, s ->
        val difference = f.second - s.second

        // if difference are ~0 consider number of covered examples (invert sign), otherwise return sign
        if (abs(difference) < Delta) s.first - f.first else sign(difference).toInt()
    }
}
