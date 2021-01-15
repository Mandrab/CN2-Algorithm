package model.expression.complex.selector

import krangl.DataFrame
import model.expression.complex.selector.categorical.Categorical
import model.expression.complex.selector.categorical.Categoricals
import model.expression.complex.selector.numerical.Numerical
import model.expression.complex.selector.numerical.Numericals

object Selectors {

    /**
     * Find attributes selectors in the given dataframe
     *
     * @param dataFrame in which search for the selectors
     * @return the founded selectors
     */
    fun findSelectors(dataFrame: DataFrame): Set<Selector> = dataFrame.cols.drop(1).flatMap { column ->
        column.values().distinct().flatMap { value ->
            when(value) {
                is Int -> Numerical.get(column.name, value.toDouble())  // numerical values to convert to double
                is Float, Double -> Numerical.get(column.name, value as Double) // correct format numerical values
                is String -> Categorical.get(column.name, value)        // correct format categorical values
                else -> Categorical.get(column.name, value.toString())  // convert others to string and use as category
            } } }.toSet()

    /**
     * Simplify the set of selectors keeping only useful ones
     *
     * @param selectors the set to simplify
     * @return the simplified set of selectors
     */
    fun simplify(vararg selectors: Selector): Iterable<Selector> {
        assert(selectors.all { it.attribute == selectors.first().attribute })

        return when {
            selectors.all { it is Numerical } -> Numericals.simplify(*selectors.map { it as Numerical }.toTypedArray())
            selectors.all { it is Categorical } ->
                Categoricals.simplify(*selectors.map { it as Categorical }.toTypedArray())
            else -> emptyList()
        }
    }
}
