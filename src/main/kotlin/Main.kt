import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import krangl.DataFrame
import krangl.readCSV
import model.CN2

const val DatasetDescription = "Path to the dataset"
const val StarSetDescription = "Size of the complex set (best complexes found so far during training). Default 15"
const val ThresholdDescription = "Default 99" // TODO help

private class Main : CliktCommand() {
    val dataset: String by argument(help=DatasetDescription)
    val starSetSize: Int by option(help=StarSetDescription).int().default(15)
    val threshold: Int by option(help=ThresholdDescription).int().default(99)

    override fun run() {
        // read data-frame from disk
        val dataframe = DataFrame.readCSV(dataset)

        val rules = CN2.run(threshold, starSetSize, dataframe)
        print(rules)
    }
}

fun main(args: Array<String>) = Main().main(args)
