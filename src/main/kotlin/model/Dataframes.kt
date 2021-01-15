package model

import krangl.DataFrame
import model.expression.complex.Complex
import model.expression.complex.Complexes
import model.expression.complex.Complexes.evaluate
import model.expression.selector.Selector

object Dataframes {

    /**
     * Produce a set of the best complexes (selectors) that cover the dataframe
     *
     * @param dataframe set of the examples to cover
     * @param selectors known selectors of the dataframe
     * @param starSetSize size of the wanted output set
     * @return set of the best complexes (selectors) that cover the dataframe
     */
    fun produceStarSet(dataframe: DataFrame, selectors: Set<Selector>, starSetSize: Int): Set<Complex> =
        selectors.map { Complex(it) }.map { it to evaluate(dataframe)(it) }.sortedWith(comparator)
            .take(starSetSize).map { it.first }.toSet()

    private val comparator: Comparator<Pair<Complex, Pair<Int, Double>>> =
        Comparator { a, b -> Complexes.comparator.compare(a.second, b.second) }
}
