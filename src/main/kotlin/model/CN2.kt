package model

import controller.Logger.info
import krangl.DataFrame
import model.Dataframes.produceStarSet
import model.complex.Complexes.bestComplex
import model.complex.Complexes.evaluate
import model.selector.Selectors.findSelectors

object CN2 {

    /**
     * Tries to find rules for classify the dataset.
     * The output is in the form of a decision-list
     */
    fun run(threshold: Int, starSetSize: Int, dataFrame: DataFrame): Set<Rule> {

        // dataset's attributes selectors
        val selectors = findSelectors(dataFrame)

        // produce the initial star-set
        val starSet = produceStarSet(dataFrame, selectors, starSetSize)

        // tail recursive function to find rules
        tailrec fun findRule(data: DataFrame, rulesSoFar: Set<Rule>): Set<Rule> {
            // search for the best complex. If no complex is found return the rules founded so far
            val bestComplex = bestComplex(starSetSize, selectors, evaluate(data)) ?: return rulesSoFar
            info("Best complex: $bestComplex")

            // separate covered and non-covered examples
            val coveredExamples = data.filterByRow { bestComplex.cover(it) }
            val uncoveredData = data.filterByRow { ! bestComplex.cover(it) }
            info("Covered examples: ${coveredExamples.nrow}")
            info("Uncovered examples: ${uncoveredData.nrow}")

            // find prevailing class in covered examples
            val prevailingClass = coveredExamples[0].values().groupBy { it }.mapValues { it.value.count() }.toList()
                .minByOrNull { it.second }!!.first!!

            // search recursively for other rules
            return findRule(uncoveredData, rulesSoFar + Rule(bestComplex, prevailingClass))
        }
        return findRule(dataFrame, emptySet())
    }
}
