package com.ankamagames.wakfu.client.core.game.exchangeMachine;

import java.util.*;

public class ExchangeEntryComparator
{
    public static final Comparator<ExchangeEntryView> AGT_ORDER;
    public static final Comparator<ExchangeEntryView> BY_COST;
    
    static {
        AGT_ORDER = new Comparator<ExchangeEntryView>() {
            @Override
            public int compare(final ExchangeEntryView o1, final ExchangeEntryView o2) {
                return o1.getOrder() - o2.getOrder();
            }
        };
        BY_COST = new Comparator<ExchangeEntryView>() {
            @Override
            public int compare(final ExchangeEntryView o1, final ExchangeEntryView o2) {
                return o1.getCostTotalSize() - o2.getCostTotalSize();
            }
        };
    }
}
