package model.expression.complex

import krangl.DataFrameRow
import model.expression.selector.Selector
import model.expression.selector.Selectors

/**
 * Represents a complex expression of attributes
 *
 * @author Paolo Baldini
 */
open class Complex(
    val selectors: Set<Selector>
) {
    companion object {
        /**
         * Construct a Complex from a set of selectors
         *
         * @param selectors set of selectors to use to build the complex
         * @return the built complex
         */
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
    fun isNull() = selectors.groupBy { it.attribute }.any { (_, ss) -> ss.any { s -> ss.any { ! s.coherent(it) } } }

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
    override fun toString(): String = "[${selectors.joinToString(prefix = "(", separator = "), (", postfix = ")")}]"

    /**
     * Check for object equality. It recursively check also for selectors equality
     *
     * @param other element to check equality with
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
