package model

import krangl.DataFrame

/**
 * Object that contains functions to evaluate inferred decision rules
 *
 * @author Paolo Baldini
 */
object Evaluator {

    fun evaluateAccuracy(data: DataFrame, rules: Set<Rule>): Double {

        val classColumn = data.names.first()

        return data.rows.groupBy { row -> rules.first { it.complex.cover(row) } }
            .map { it.value.filter { row -> row[classColumn] == it.key.resultingClass }.count() }
            .sum() * 100.0 / data.nrow
    }
}
