package controller

/**
 * Log execution information
 *
 * @author Paolo Baldini
 */
object Logger {
    /** Default 'false'. Set it to 'true' to log non-error information */
    var verbosityLevel = 1

    /**
     * Log information to stdout if verbose is set to 'true'
     *
     * @param string element to print
     */
    fun info(string: Any, level: Int = 1) { if (verbosityLevel <= level) println(string) }

    /**
     * Log information to stderr. Does not require verbose set to 'true'
     *
     * @param string element to print
     */
    fun error(string: Any) = System.err.println(string)
}
