package model.expression.complex

import krangl.DataFrameRow
import model.expression.selector.Selector
import model.expression.selector.Selectors

open class Complex(
    val selectors: Set<Selector>
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
    fun cover(row: DataFrameRow) = selectors.all { selector -> selector.test(row[selector.attribute]) }

    /**
     * Specialize a complex with the given selector. Do not check for coherence
     *
     * @param selector to use to specialize the complex
     * @return the specialized complex
     */
    fun specialize(selector: Selector) = Complex(selectors + selector)

    /**
     * Check if the complex is coherent (i.e., does not have discordant selectors)
     *
     * @return true if the complex is not coherent
     */
    fun isNull() = ! selectors.groupBy { it.attribute }.all {
        it.value.all { selector -> it.value.all { selector.coherent(it) } } }

    /**
     * Simplify a non-null complex. Does not check for nullability
     *
     * @return the simplified complex
     */
    fun simplify(): Complex = Complex(selectors.groupBy { it.attribute }
        .flatMap { Selectors.simplify(*it.value.toTypedArray()) }.toSet())

    /**
     * Get human readable form of the data
     *
     * @return string representing the object
     */
    override fun toString(): String = "Complex:\n" + selectors.joinToString(prefix = "\t", postfix = "\n")

    /**
     * Check for object equality. It recursively check also for selectors equality
     *
     * @return true if the objects (complexes) are equal
     */
    override fun equals(other: Any?): Boolean = other is Complex
            && other.selectors.size == selectors.size && other.selectors.all { selectors.contains(it) }

    /**
     * Generate hash code for the complex
     *
     * @return hash code of the complex
     */
    override fun hashCode(): Int = selectors.hashCode()
}
