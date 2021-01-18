package controller

import krangl.DataFrame
import krangl.readCSV
import model.CN2
import model.Evaluator
import java.io.File

fun main() = listOf(
    "res/datasets/breast_cancer/breast-cancer.data",
    "res/datasets/lymphography/lymphography.data",
    "res/datasets/primary_tumor/primary-tumor.data"
).map { File(it) }.forEach { path ->
    // set logger verbosity to max (print less possible)
    Logger.verbosityLevel = Int.MAX_VALUE

    // read data-frame from disk
    val dataframe = DataFrame.readCSV(path)
    dataframe.filterByRow { ! it.containsValue("?") }

    // split the data in test and training
    val data = Datasets.split(dataframe, 70)
    val training = data.first
    val test = data.second

    listOf(99).forEach { threshold ->
        listOf(5, 10).forEach { sSize ->
            val time = System.currentTimeMillis()
            val rules = CN2.run(threshold, sSize, training)
            println(
                "${path.name} & " +
                "$threshold & " +
                "$sSize & " +
                "%.2f &".format(Evaluator.evaluateAccuracy(training, rules)) +
                "%.2f &".format(Evaluator.evaluateAccuracy(test, rules)) + " & ${System.currentTimeMillis() - time}" +
                rules.size + " \\\\")
        }
    }
}
