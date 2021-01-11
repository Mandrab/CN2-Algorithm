package model.selector

interface Selector {

    val attribute: String

    fun test(element: Any): Boolean
}
