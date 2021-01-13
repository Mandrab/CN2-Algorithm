package model

import krangl.DataFrameRow
import model.selector.Selector

class Complex(
    private val selectors: Set<Selector>
) {
    companion object {
        operator fun invoke(vararg selectors: Selector) = Complex(setOf(*selectors))
    }

    /**
     * Check if complex cover a given row (i.e., the row match all the complex's selectors)
     *
     * @param row to which check covering
     * @return true if the complex cover the row
     */
    fun cover(row: DataFrameRow) = row.entries.all { (attribute, value) ->
        selectors.filter { it.attribute == attribute }.all { it.test(value) } }

    /**
     * Specialize a complex with the given selector
     *
     * @param selector to use to specialize the complex
     * @return the specialized complex
     */
    fun specialize(selector: Selector) = Complex(selectors + selector) // TODO check if sets are ordered

    /**
     * Check if the complex is coherent (i.e., does not have discordant selectors)
     *
     * @return true if the complex is coherent
     */
    fun isNull() = selectors.groupBy { it.attribute }.all {
        it.value.all { selector -> it.value.all { selector.coherent(it) } } }
}
