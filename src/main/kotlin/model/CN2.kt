package model

import krangl.DataFrame
import model.Dataframes.bestComplex
import model.Dataframes.evaluate
import model.Dataframes.produceStarSet
import model.complex.Complex
import model.selector.Selectors.findSelectors

object CN2 {

    /**
     * Tries to find rules for classify the dataset.
     * The output is in the form of a decision-list
     */
    fun run(threshold: Int, starSetSize: Int, dataFrame: DataFrame): Set<Rule> {
        // TODO implementation of CN2 induction algorithm

        // dataset's attributes selectors
        val selectors = findSelectors(dataFrame)
        print(selectors.joinToString(separator = "\n"))

        val starSet = produceStarSet(dataFrame, selectors, starSetSize)

        // tail recursive function to find rules
        tailrec fun findRule(data: DataFrame, starSet: Set<Complex>, rulesSoFar: Set<Rule>): Set<Rule> {
            // search for the best complex. If no complex is found return the rules founded so far
            val bestComplex = bestComplex(starSet, starSetSize, selectors) { evaluate(data, it) } ?: return rulesSoFar

            // separate covered and non-covered examples
            val coveredExamples = data.filterByRow { bestComplex.cover(it) }
            val data = data.filterByRow { !bestComplex.cover(it) }

            // find prevailing class in covered examples
            val prevailingClass = coveredExamples[0].values().groupBy { it }.mapValues { it.value.count() }.toList()
                .minByOrNull { it.second }!!.first!!

            // search recursively for other rules
            return findRule(data, starSet, rulesSoFar + Rule(bestComplex, prevailingClass))
        }
        return findRule(dataFrame, starSet, emptySet())
    }
}
