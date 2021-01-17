package model

import controller.Logger.info
import krangl.DataFrame
import model.expression.complex.Complex
import model.expression.complex.Complexes.bestComplex
import model.expression.complex.Complexes.classDistribution
import model.expression.complex.Complexes.entropy
import model.expression.complex.Complexes.evaluate
import model.expression.complex.Complexes.significance
import model.expression.selector.Selectors.findSelectors

/**
 * CN2 algorithm's main file.
 * This algorithm try to extract a rule-list that can classify the given dataset
 *
 * @author Paolo Baldini
 */
object CN2 {

    /**
     * Tries to find rules for classify the dataset.
     * The output is in the form of a decision-list
     *
     * @param threshold TODO
     * @param starSetSize size of the star-set during search of the best complex
     * @param dataFrame set of examples to classify
     * @return set of rules that classify the given dataset
     */
    fun run(threshold: Int, starSetSize: Int, dataFrame: DataFrame): Set<Rule> {

        // find attribute selectors for the given dataset
        val selectors = findSelectors(dataFrame)

        // get the probability distribution of classes in the dataframe
        val classesDistribution = classDistribution(dataFrame)

        // set up evaluation functions
        val entropyFunction = evaluate(entropy)
        val significanceFunction = evaluate(significance(classesDistribution))

        /**
         * Tail recursive (does not fills the stack) function to find rules
         * It keeps iterate until the dataframe is empty or a blocking condition is found
         *
         * @param data dataset of examples to classify
         * @param rulesSoFar set of rules found until now
         */
        tailrec fun findRule(data: DataFrame, rulesSoFar: Set<Rule>): Set<Rule> {
            // if the dataset is empty return the rules found so far
            if (data.nrow == 0) return rulesSoFar

            // search for the best complex. If no complex is found return the rules founded so far
            val bestComplex = bestComplex(selectors, starSetSize, threshold / 100.0, entropyFunction(data),
                significanceFunction(data)) ?: return rulesSoFar
            info("Best complex: $bestComplex")

            // separate covered and non-covered examples
            val coveredExamples = data.filterByRow { bestComplex.cover(it) }
            val uncoveredData = data.filterByRow { ! bestComplex.cover(it) }
            info("Covered examples: ${coveredExamples.nrow}")
            info("Uncovered examples: ${uncoveredData.nrow}")

            // find prevailing class in covered examples
            val prevailingClass = coveredExamples[0].values().groupBy { it }.mapValues { it.value.count() }.toList()
                .minByOrNull { it.second }!!.first!!

            // search recursively for other rules on the uncovered data
            return findRule(uncoveredData, rulesSoFar + Rule(bestComplex, prevailingClass))
        }

        // return rules list with default rule as last
        return findRule(dataFrame, emptySet()) + Rule(Complex(), classesDistribution.maxByOrNull { it.value }!!.key)
    }
}
