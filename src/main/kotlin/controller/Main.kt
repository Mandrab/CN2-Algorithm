package controller

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import controller.Datasets.split
import controller.Logger.info
import krangl.DataFrame
import krangl.readCSV
import model.CN2
import model.Evaluator
import java.io.File

const val DefaultOutputFile = "rules.dat"

const val DatasetDescription = "Path to the dataset to use for the training"
const val ExportDescription = "Export the inferred rules to file ('${DefaultOutputFile}' by default)"
const val OutputFileDescription = "Path to the file in which save the inferred rules"
const val StarSetDescription = "Size of the set used to find the best complex. Default 15"
const val ThresholdDescription = "Minimum level of significance to accept a complex. Default 99"
const val VerboseDescription = "Level of verbosity during the training (0 is the max level of verbosity)"

/**
 * Main class of the program. It manages the setup of the cli parameters
 *
 * @author Paolo Baldini
 */
private class Main : CliktCommand() {
    val dataset: File by argument(help = DatasetDescription).file(mustExist = true, mustBeReadable = true)
    val export: Boolean by option("-e", "--export", help = ExportDescription).flag(default = false)
    val outputFile: File by option("-o", "--output", help = OutputFileDescription).file(mustBeWritable = true)
        .default(File(DefaultOutputFile))
    val starSetSize: Int by option("-s", "--star-set-size", help = StarSetDescription).int().default(10)
    val threshold: Int by option("-t", "--threshold", help = ThresholdDescription).int().default(99)
    val verbosityLevel: Int by option("-v", "--verbose-level", help = VerboseDescription).int().default(1)

    // run the program with the given parameters
    override fun run() {
        // set logger verbosity based on inputs
        Logger.verbosityLevel = verbosityLevel

        // read data-frame from disk
        val dataframe = DataFrame.readCSV(dataset)

        // split the data in test and training
        val data = split(dataframe, 70)
        val training = data.first
        val test = data.second

        // run the training algorithm keeping track of the time required
        val time = System.currentTimeMillis()
        val rules = CN2.run(threshold, starSetSize, training)
        info("Training time: ${System.currentTimeMillis() - time}")

        // save inferred rules to file and print them
        if (export) outputFile.writeText(rules.joinToString(separator = "\n"))
        if (!export || verbosityLevel == 0) info(rules.joinToString(separator = "\n"))

        // evaluate rules accuracy on the classification of the dataset
        info("Score of the rules in training set: ${Evaluator.evaluateAccuracy(training, rules)}")
        info("Score of the rules in test set: ${Evaluator.evaluateAccuracy(test, rules)}")
    }
}

fun main(args: Array<String>) = Main().main(args)
