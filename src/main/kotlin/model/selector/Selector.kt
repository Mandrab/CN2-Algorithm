package model.selector

interface Selector {

    val attribute: String

    fun test(element: Any?): Boolean

    /**
     * Check the selector contradict this one
     *
     * @param selector to which check  contradiction
     * @return true if the two selectors contradict each other
     */
    fun coherent(selector: Selector): Boolean
}
