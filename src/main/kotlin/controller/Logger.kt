package controller

object Logger {
    var verbose = false

    fun info(string: String) { if (verbose) println(string) }

    fun error(string: String) = System.err.println(string)
}
