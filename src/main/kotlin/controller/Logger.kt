package controller

object Logger {
    var verbose = false

    fun info(string: Any) { if (verbose) println(string) }

    fun error(string: Any) = System.err.println(string)
}
