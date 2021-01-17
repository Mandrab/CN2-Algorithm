package controller

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import controller.Logger.info
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.dataFrameOf
import krangl.readCSV
import model.CN2
import model.Evaluator
import model.expression.complex.Complexes.classDistribution
import java.io.File

const val DatasetDescription = "Path to the dataset to use for the training"
const val ExportDescription = "Export the inferred rules to file"
const val OutputFileDescription = "Path to the file in which save the inferred rules"
const val StarSetDescription = "Size of the set used to find the best complex. Default 15"
const val ThresholdDescription = "Default 99" // TODO help
const val VerboseDescription = "Log information about iterations"

const val DefaultOutputFile = "rules.dat"

/**
 * Main class of the program. It manages the setup of the cli parameters
 *
 * @author Paolo Baldini
 */
private class Main : CliktCommand() {
    val dataset: File by argument(help = DatasetDescription).file(mustExist = true, mustBeReadable = true)
    val export: Boolean by option(help = ExportDescription).flag(default = false)
    val outputFile: File by option(help = OutputFileDescription).file(mustBeWritable = true).default(File(DefaultOutputFile))
    val starSetSize: Int by option(help = StarSetDescription).int().default(15)
    val threshold: Int by option(help = ThresholdDescription).int().default(99)
    val verbose: Boolean by option(help = VerboseDescription).flag(default = false)

    override fun run() {
        // set logger verbosity based on inputs
        Logger.verbose = verbose

        // read data-frame from disk
        val dataframe = DataFrame.readCSV(dataset)

        // separate training and test sets homogeneously
        val separatedDataset = dataframe.rows.groupBy { it[dataframe.names.first()] }.map {
            val rows = it.value.shuffled()
            Triple(it.key, rows.take(rows.size * 7 / 10), rows.takeLast(rows.size * 3 / 10))
        }
        val training = dataFrameOf(separatedDataset.fold(emptyList<DataFrameRow>()) { acc,tuple -> acc + tuple.second })
        info("Training set size: ${training.nrow}. Probability distribution: ${classDistribution(training)}")
        val test = dataFrameOf(separatedDataset.fold(emptyList<DataFrameRow>()) { acc, tuple -> acc + tuple.third })
        info("Test set size: ${test.nrow}. Probability distribution: ${classDistribution(test)}")

        // run the training algorithm keeping track of the time required
        val time = System.currentTimeMillis()
        val rules = CN2.run(threshold, starSetSize, training)
        info("Training time: ${System.currentTimeMillis() - time}")

        // save inferred rules to file and print them
        if (export) outputFile.writeText(rules.joinToString(separator = "\n"))
        if (!export || verbose) info(rules.joinToString(separator = "\n"))

        // evaluate rules accuracy on the classification of the dataset
        info("Score of the rules: ${Evaluator.evaluateAccuracy(test, rules)}")
    }
}

fun main(args: Array<String>) = Main().main(args)
