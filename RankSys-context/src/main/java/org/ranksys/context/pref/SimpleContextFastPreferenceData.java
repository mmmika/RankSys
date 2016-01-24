/* 
 * Copyright (C) 2015 RankSys http://ranksys.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ranksys.context.pref;

import es.uam.eps.ir.ranksys.fast.index.FastItemIndex;
import es.uam.eps.ir.ranksys.fast.index.FastUserIndex;
import es.uam.eps.ir.ranksys.fast.preference.AbstractFastPreferenceData;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import static java.lang.Integer.compare;
import static java.util.Collections.binarySearch;
import static java.util.Collections.sort;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.ranksys.context.pref.ContextFastPreferenceData.IdxPrefCtx;

/**
 * Simple implementation of FastPreferenceData backed by nested lists.
 *
 * @author Saúl Vargas (saul.vargas@uam.es)
 *
 * @param <U> type of the users
 * @param <I> type of the items
 */
public class SimpleContextFastPreferenceData<U, I, C> extends AbstractFastPreferenceData<U, I> implements ContextFastPreferenceData<U, I, C> {

    private final int numPreferences;
    private final List<List<IdxPrefCtx<C>>> uidxList;
    private final List<List<IdxPrefCtx<C>>> iidxList;
    private final int contextSize;

    /**
     * Constructor.
     *
     * @param numPreferences number of total preferences
     * @param uidxList list of lists of preferences by user index
     * @param iidxList list of lists of preferences by item index
     * @param uIndex user index
     * @param iIndex item index
     */
    public SimpleContextFastPreferenceData(int numPreferences, List<List<IdxPrefCtx<C>>> uidxList, List<List<IdxPrefCtx<C>>> iidxList, FastUserIndex<U> uIndex, FastItemIndex<I> iIndex, int contextSize) {
        super(uIndex, iIndex);
        this.numPreferences = numPreferences;
        this.uidxList = uidxList;
        this.iidxList = iidxList;
        this.contextSize = contextSize;

        uidxList.stream().filter(list -> list != null).forEach(list -> sort(list, (p1, p2) -> compare(p1.idx, p2.idx)));
        iidxList.stream().filter(list -> list != null).forEach(list -> sort(list, (p1, p2) -> compare(p1.idx, p2.idx)));
    }

    @Override
    public int numUsers(int iidx) {
        if (iidxList.get(iidx) == null) {
            return 0;
        }
        return iidxList.get(iidx).size();
    }

    @Override
    public int numItems(int uidx) {
        if (uidxList.get(uidx) == null) {
            return 0;
        }
        return uidxList.get(uidx).size();
    }

    @Override
    public Stream<IdxPrefCtx<C>> getUidxPreferences(int uidx) {
        if (uidxList.get(uidx) == null) {
            return Stream.empty();
        } else {
            return uidxList.get(uidx).stream();
        }
    }

    @Override
    public Stream<IdxPrefCtx<C>> getIidxPreferences(int iidx) {
        if (iidxList.get(iidx) == null) {
            return Stream.empty();
        } else {
            return iidxList.get(iidx).stream();
        }
    }

    @Override
    public int numPreferences() {
        return numPreferences;
    }

    @Override
    public IntStream getUidxWithPreferences() {
        return IntStream.range(0, numUsers())
                .filter(uidx -> uidxList.get(uidx) != null);
    }

    @Override
    public IntStream getIidxWithPreferences() {
        return IntStream.range(0, numItems())
                .filter(iidx -> iidxList.get(iidx) != null);
    }

    @Override
    public int numUsersWithPreferences() {
        return (int) uidxList.stream()
                .filter(iv -> iv != null).count();
    }

    @Override
    public int numItemsWithPreferences() {
        return (int) iidxList.stream()
                .filter(iv -> iv != null).count();
    }

    @Override
    public Stream<IdPrefCtx<I, C>> getUserPreferences(U u) {
        return getUidxPreferences(user2uidx(u)).map(iv -> new IdPrefCtx<>(iidx2item(iv.idx), iv.v, iv.cs));
    }

    @Override
    public Stream<IdPrefCtx<U, C>> getItemPreferences(I i) {
        return getIidxPreferences(item2iidx(i)).map(uv -> new IdPrefCtx<>(uidx2user(uv.idx), uv.v, uv.cs));
    }

    @Override
    public IdxPrefCtx<C> getPreference(int uidx, int iidx) {
        List<IdxPrefCtx<C>> list = uidxList.get(uidx);
        int i = binarySearch(list, new IdxPrefCtx<>(iidx, 0.0, null), (p1, p2) -> compare(p1.idx, p2.idx));
        if (i < 0) {
            return null;
        } else {
            return list.get(i);
        }
    }

    @Override
    public int getContextSize() {
        return contextSize;
    }

    @Override
    public IntIterator getUidxIidxs(int uidx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleIterator getUidxVs(int uidx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntIterator getIidxUidxs(int iidx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DoubleIterator getIidxVs(int iidx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean useIteratorsPreferentially() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IdPrefCtx<I, C> getPreference(U u, I i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
