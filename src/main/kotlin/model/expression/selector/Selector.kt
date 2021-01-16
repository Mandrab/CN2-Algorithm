package model.expression.selector

/**
 * Defines the schema of a selector
 *
 * @author Paolo Baldini
 */
interface Selector {

    val attribute: String   // attribute represented by the selector

    /**
     * Test an element to check if it is covered by the selector
     *
     * @param element to test
     * @return true if the element is covered by the selector
     */
    fun test(element: Any?): Boolean

    /**
     * Check if the selector is coherent with this one
     *
     * @param selector to which check contradiction
     * @return true if the two selectors does not contradict each other
     */
    fun coherent(selector: Selector): Boolean
}
