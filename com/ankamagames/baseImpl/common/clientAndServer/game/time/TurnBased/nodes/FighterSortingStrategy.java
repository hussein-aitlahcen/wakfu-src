package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes;

import gnu.trove.*;
import java.util.*;

public abstract class FighterSortingStrategy
{
    protected abstract void sortInitially(final DynamicLists p0);
    
    protected abstract void sortForOneTurn(final DynamicLists p0);
    
    protected abstract void sortDynamically(final DynamicLists p0);
    
    protected abstract void addToDynamicList(final DynamicLists p0, final long p1);
    
    protected abstract void insertIntoTurnOrderList(final DynamicLists p0, final long p1, final int p2);
    
    protected abstract void removeFromDynamicList(final DynamicLists p0, final long p1);
    
    protected void sortByComparator(final TLongArrayList list, final Comparator<Long> fighterComparator) {
        final long[] values = list.toNativeArray();
        list.clear();
        for (final long value : values) {
            this.insertInOrder(list, value, fighterComparator);
        }
    }
    
    protected void insertInOrder(final TLongArrayList list, final long value, final Comparator<Long> fighterComparator) {
        this.insertInOrder(list, value, fighterComparator, 0);
    }
    
    protected void insertInOrder(final TLongArrayList list, final long value, final Comparator<Long> fighterComparator, int insertPos) {
        while (insertPos < list.size() && fighterComparator.compare(value, list.get(insertPos)) >= 0) {
            ++insertPos;
        }
        list.insert(insertPos, value);
    }
}
