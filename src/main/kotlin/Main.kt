import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import krangl.DataFrame
import krangl.readCSV
import model.CN2

private class Main : CliktCommand() {
    val dataset: String by argument(help="Path of the dataset")
    val starsetSize: Int by option(help="Default 15").int().default(15) // TODO help
    val threshold: Int by option(help="Default 99").int().default(99) // TODO help

    override fun run() {
        // read data-frame from disk
        val dataframe = DataFrame.readCSV(dataset)

        val rules = CN2.run(threshold, starsetSize, dataframe)
        print(rules)
    }
}

fun main(args: Array<String>) = Main().main(args)