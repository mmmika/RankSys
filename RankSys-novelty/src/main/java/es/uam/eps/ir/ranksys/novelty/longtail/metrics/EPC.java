/* 
 * Copyright (C) 2015 Information Retrieval Group at Universidad Autónoma
 * de Madrid, http://ir.ii.uam.es
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package es.uam.eps.ir.ranksys.novelty.longtail.metrics;

import es.uam.eps.ir.ranksys.novdiv.itemnovelty.metrics.ItemNoveltyMetric;
import es.uam.eps.ir.ranksys.novelty.longtail.PCItemNovelty;
import es.uam.eps.ir.ranksys.metrics.rank.RankingDiscountModel;
import es.uam.eps.ir.ranksys.metrics.rel.RelevanceModel;

/**
 * Expected popularity complement metric.
 *
 * S. Vargas. Novelty and diversity evaluation and enhancement in Recommender
 * Systems. PhD Thesis.
 * 
 * S. Vargas and P. Castells. Rank and relevance in novelty and diversity for
 * Recommender Systems. RecSys 2011.
 *
 * @author Saúl Vargas (saul.vargas@uam.es)
 * @author Pablo Castells (pablo.castells@uam.es)
 * 
 * @param <U> type of the users
 * @param <I> type of the items
 */
public class EPC<U, I> extends ItemNoveltyMetric<U, I> {

    /**
     * Constructor.
     *
     * @param cutoff maximum size of the recommendation list that is evaluated
     * @param novelty novelty model
     * @param relModel relevance model
     * @param disc ranking discount model
     */
    public EPC(int cutoff, PCItemNovelty<U, I> novelty, RelevanceModel<U, I> relModel, RankingDiscountModel disc) {
        super(cutoff, novelty, relModel, disc);
    }

}
