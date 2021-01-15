package controller

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import krangl.DataFrame
import krangl.readCSV
import model.CN2
import java.io.File

const val DatasetDescription = "Path to the dataset"
const val StarSetDescription = "Size of the complex set (best complexes found so far during training). Default 15"
const val ThresholdDescription = "Default 99" // TODO help
const val VerboseDescription = "Log iterations information"

private class Main : CliktCommand() {
    val dataset: File by argument(help = DatasetDescription).file(mustExist = true, mustBeReadable = true)
    val starSetSize: Int by option(help = StarSetDescription).int().default(15)
    val threshold: Int by option(help = ThresholdDescription).int().default(99)
    val verbose: Boolean by option(help = VerboseDescription).flag(default = false)

    override fun run() {
        // set logger verbosity based on inputs
        Logger.verbose = verbose

        // read data-frame from disk
        val dataframe = DataFrame.readCSV(dataset)

        val rules = CN2.run(threshold, starSetSize, dataframe)
        print(rules)
    }
}

fun main(args: Array<String>) = Main().main(args)
