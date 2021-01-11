package model

import krangl.DataFrame
import model.Dataframes.bestComplex
import model.Dataframes.cover
import model.selector.Selector
import model.selector.categorical.Categorical.Categorical
import model.selector.numerical.Numerical.Numerical

object CN2 {

    /**
     * Tries to find rules for classify the dataset.
     * The output is in the form of a decision-list
     */
    fun run(threshold: Int, starsetSize: Int, dataFrame: DataFrame): List<Rule> {
        // TODO implementation of CN2 induction algorithm

        val selectors = findSelectors(dataFrame)
        print(selectors.joinToString(separator = "\n"))

        val rules = emptyList<Rule>()

        // tail recursive
        tailrec fun findRule(dataFrame: DataFrame, selectors: Set<Selector>, rulesSoFar: Set<Rule>): Set<Rule> {
            val bestComplex = bestComplex(dataFrame, selectors) ?: return emptySet()

            // separate covered and non-covered examples
            val coveredExamples = dataFrame.filterByRow { bestComplex.cover(it) }
            val dataFrame = dataFrame.filterByRow { !bestComplex.cover(it) }

            // find prevailing class
            val prevailingClass = coveredExamples.get(0).values().groupBy { it }.mapValues { it.count() }.toList()
                .sortedBy { it.second }.first().first
            return findRule(dataFrame, selectors, rulesSoFar + Rule(bestComplex, prevailingClass))
        }
        return findRule(dataFrame, selectors, emptySet())
    }

    private fun findSelectors(dataFrame: DataFrame): Set<Selector> = dataFrame.cols.drop(1)
        .flatMap { column -> column.values().distinct().flatMap { value -> when(value) {
            is String -> Categorical.get(column.name, value)
            is Int -> Numerical.get(column.name, value.toDouble())
            else -> Numerical.get(column.name, value as Double)
        } } }.toSet()

}
