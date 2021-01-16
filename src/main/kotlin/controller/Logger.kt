package controller

/**
 * Log execution information
 *
 * @author Paolo Baldini
 */
object Logger {
    /** Default 'false'. Set it to 'true' to log non-error information */
    var verbose = false

    /**
     * Log information to stdout if verbose is set to 'true'
     *
     * @param string element to print
     */
    fun info(string: Any) { if (verbose) println(string) }

    /**
     * Log information to stderr. Does not require verbose set to 'true'
     *
     * @param string element to print
     */
    fun error(string: Any) = System.err.println(string)
}
