package model.complex

import krangl.DataFrame
import model.selector.Selector
import java.util.*
import kotlin.math.log2

object Complexes {

    /**
     * Evaluate the complex quality in the coverage of the dataframe
     *
     * @param dataframe set of the examples to cover
     * @param complex to evaluate
     * @return a double value that represent the coverage. The lower it is, the better it is
     */
    fun evaluate(data: DataFrame): (Complex) -> Double = { complex ->
            val coveredRows = data.filterByRow { complex.cover(it) }.rows   // takes only rows covered by the complex
            val coveredRowsCount = coveredRows.count().toDouble()           // count examples covered
            - coveredRows.groupBy { it[data.names.first()] }                  // group by class (first column)
                .map { it.value.count() / coveredRowsCount }                // to probability distribution
                .map { it * log2(it) }.sum()                                // calculate entropy
        }

    /**
     * Find the best complex specializing the star-set
     *
     * @param starSet to specialize
     * @param starSetSize required size of the output star-set
     * @param selectors to use for the specialization
     * @param evaluate strategy for the complexes evaluation
     * @return best complex or null if there isn't any available complex
     */
    fun bestComplex(starSetSize: Int, selectors: Set<Selector>, evaluate: (Complex) -> Double): Complex? {

        var newStarSet = selectors.map { Complex(it) }.sortedBy(evaluate).take(starSetSize).asSequence() //starSet.asSequence()
        var bestComplex: Complex? = null
        var bestScore = Double.POSITIVE_INFINITY

        // iterate till the star-set has items
        do {
            newStarSet = newStarSet
                .flatMap { complex -> selectors.map { complex.specialize(it) } }// specialize all the complexes
                .filterNot { it.isNull() }                                      // remove incoherent (null) complexes
                //.map { it.simplify() }
                .filterNot { newStarSet.contains(it) }                          // remove complexes present in star
                .toSortedSet(Comparator.comparingDouble { evaluate(it) })       // order complexes by score
                .take(starSetSize)                                              // take complexes to fill the star-set
                .asSequence()

            newStarSet.firstOrNull()?.let {
                val score = evaluate(it)
                if (score < bestScore) {
                    bestComplex = it
                    bestScore = score
                }
            }
        } while (newStarSet.firstOrNull() != null)
        /*while (newStarSet.firstOrNull() != null) {
            newStarSet = newStarSet
                .flatMap { complex -> selectors.map { complex.specialize(it) } }// specialize all the complexes
                .filterNot { newStarSet.contains(it) }                          // remove all already present complexes
                .filterNot { it.isNull() }                                      // remove incoherent (null) complexes
                .toSortedSet(Comparator.comparingDouble { evaluate(it) })       // order complexes by score
                .take(starSetSize)                                              // take complexes to fill the star-set
                .asSequence()

            newStarSet.firstOrNull()?.let {
                val score = evaluate(it)
                if (score < bestScore) {
                    bestComplex = it
                    bestScore = score
                }
            }
        }*/
        return bestComplex
    }
}
