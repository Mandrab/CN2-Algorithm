package model.selector

import krangl.DataFrame
import model.selector.categorical.Categorical
import model.selector.numerical.Numerical

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
}
