package model

import krangl.DataFrame
import model.complex.Complex
import model.complex.Complexes.evaluate
import model.selector.Selector

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
        selectors.map { Complex(it) }.sortedByDescending { evaluate(dataframe, it) }.take(starSetSize).toSet()
}
