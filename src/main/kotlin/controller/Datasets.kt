package controller

import controller.Logger.info
import krangl.DataFrame
import krangl.DataFrameRow
import krangl.dataFrameOf
import model.expression.complex.Complexes.classDistribution

/**
 * Utilities to prepare datasets
 *
 * @author Paolo Baldini
 */
object Datasets {

    /**
     * Homogeneously split the dataset in two set where the first has the specified percentage of data
     *
     * @param dataFrame to split
     * @param percentage of data that should be given to the first set
     * @return the two disjoint sets
     */
    fun split(dataFrame: DataFrame, percentage: Int): Pair<DataFrame, DataFrame> {
        // get class column name in the dataset
        val classColumn = dataFrame.names.first()

        // separate the dataset homogeneously
        val separatedDataset = dataFrame.rows.groupBy { it[classColumn] }.map {
            val rows = it.value.shuffled()
            val leftSize = rows.size * percentage / 100
            val rightSize = rows.size * (100 - percentage) / 100
            rows.take(leftSize) to rows.takeLast(rightSize)
        }

        // create the return value
        val left = dataFrameOf(separatedDataset.fold(emptyList<DataFrameRow>()) { acc,tuple -> acc + tuple.first })
        val right = dataFrameOf(separatedDataset.fold(emptyList<DataFrameRow>()) { acc, tuple -> acc + tuple.second })

        // print info about the sets
        info("First set of size: ${left.nrow} with probability distribution of: ${classDistribution(left)}")
        val test = dataFrameOf(separatedDataset.fold(emptyList<DataFrameRow>()) { acc, tuple -> acc + tuple.second })
        info("Test set of size: ${right.nrow} with probability distribution of: ${classDistribution(right)}")

        return left to right
    }
}
