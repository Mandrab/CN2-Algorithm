package model

import krangl.DataFrameRow
import model.selector.Selector

class Complex(
    private val selectors: Set<Selector>
) {

    fun cover(row: DataFrameRow) = row.entries.all { (attribute, value) ->
        selectors.filter { it.attribute == attribute }.all { it.test(value) } }
}
