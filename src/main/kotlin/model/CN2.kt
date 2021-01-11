package model

import krangl.DataFrame
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

        val bestComplex = TODO(dataFrame, selectors)
        while (bestComplex != null && dataFrame.nrow > 0) {
            // ce := coveredExamples()
            // ds = ds.Filter(not in ce)
            // c := prevailingClass(ce)
            // rules = append(rules, Rule(bc,c))
        }
        return rules
    }

    private fun findSelectors(dataFrame: DataFrame): Set<Selector> = dataFrame.cols.drop(1)
        .flatMap { column -> column.values().distinct().flatMap { value -> when(value) {
            is String -> Categorical.get(column.name, value)
            is Int -> Numerical.get(column.name, value.toDouble())
            else -> Numerical.get(column.name, value as Double)
        } } }.toSet()

}
