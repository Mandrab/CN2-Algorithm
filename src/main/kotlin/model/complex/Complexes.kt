package model.complex

import krangl.DataFrame
import model.selector.Selector
import kotlin.math.log2

object Complexes {

    /**
     * Evaluate the complex quality in the coverage of the dataframe
     *
     * @param dataframe set of the examples to cover
     * @param complex to evaluate
     * @return a double value that represent the coverage. The lower it is, the better it is
     */
    fun evaluate(dataframe: DataFrame, complex: Complex): Double {
        val classCount = dataframe[0].values().distinct().count()   // the class is supposed to be in the first column
        val coveredExamples = dataframe.rows.filter { complex.cover(it) }
        val probabilityDistribution = coveredExamples.groupBy { it[dataframe.names[0]] }
            .map { it.value.count().toDouble() / classCount }
        return - probabilityDistribution.map { it * log2(it) }.sum()    // information-theoretic entropy
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
    fun bestComplex(starSet: Set<Complex>, starSetSize: Int, selectors: Set<Selector>, evaluate: (Complex) -> Double)
            : Complex? {

        var bestComplex: Complex? = null
        var bestScore = Double.NEGATIVE_INFINITY

        while (starSet.isNotEmpty()) {
            starSet.asSequence()                                                    // sequence improve performance
                .flatMap { complex -> selectors.map { complex.specialize(it) } }    // new-star set
                .filterNot { starSet.contains(it) }                                 // filter unspecialized complexes
                .filterNot { it.isNull() }                                          // filter incoherent complexes
                //.filter { TODO() } // statisticallySignificant... probably not needed
                .sortedBy { evaluate(it) }                                          // order by best performance
                .take(starSetSize)                                                  // take only needed element

            if (bestScore < evaluate(starSet.first())) {
                bestComplex = starSet.first()
                bestScore = evaluate(bestComplex)
            }
        }
        return bestComplex
    }
}