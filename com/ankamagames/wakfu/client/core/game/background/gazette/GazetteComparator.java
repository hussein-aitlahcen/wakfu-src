package com.ankamagames.wakfu.client.core.game.background.gazette;

import java.util.*;

class GazetteComparator
{
    public static final Comparator<GazetteView> ID_COMPARATOR_DIRECT;
    public static final Comparator<GazetteView> ID_COMPARATOR_INDIRECT;
    
    static {
        ID_COMPARATOR_DIRECT = new Comparator<GazetteView>() {
            @Override
            public int compare(final GazetteView o1, final GazetteView o2) {
                return o1.getId() - o2.getId();
            }
        };
        ID_COMPARATOR_INDIRECT = new Comparator<GazetteView>() {
            @Override
            public int compare(final GazetteView o1, final GazetteView o2) {
                return o2.getId() - o1.getId();
            }
        };
    }
}
