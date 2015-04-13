package com.ankamagames.wakfu.common.game.market.constant;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.market.*;

public enum MarketEntryComparator
{
    PRICE_CRESCENT(1, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            if (o1.getPackPrice() != o2.getPackPrice()) {
                return o1.getPackPrice() - o2.getPackPrice();
            }
            return o1.compareReleaseDate(o2);
        }
    }), 
    PRICE_DESCENDING(2, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            if (o1.getPackPrice() != o2.getPackPrice()) {
                return o2.getPackPrice() - o1.getPackPrice();
            }
            return o2.compareReleaseDate(o1);
        }
    }), 
    SELLER_CRESCENT(3, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.getSellerName().compareTo(o2.getSellerName());
        }
    }), 
    SELLER_DESCENDING(4, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o2.getSellerName().compareTo(o1.getSellerName());
        }
    }), 
    PACK_NUMBER_CRESCENT(5, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.getPackNumber() - o2.getPackNumber();
        }
    }), 
    PACK_NUMBER_DESCENDING(6, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o2.getPackNumber() - o1.getPackNumber();
        }
    }), 
    PACK_TYPE_CRESCENT(7, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.getPackType().qty - o2.getPackType().qty;
        }
    }), 
    PACK_TYPE_DESCENDING(8, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o2.getPackType().qty - o1.getPackType().qty;
        }
    }), 
    RELEASE_DATE_CRESCENT(9, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.compareReleaseDate(o2);
        }
    }), 
    RELEASE_DATE_DESCENDING(10, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o2.compareReleaseDate(o1);
        }
    }), 
    REMAINING_TIME_CRESCENT(11, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.compareRemainingTime(o2);
        }
    }), 
    REMAINING_TIME_DESCENDING(12, (Comparator<MarketEntry>)new Comparator<MarketEntry>() {
        @Override
        public int compare(final MarketEntry o1, final MarketEntry o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o2.compareRemainingTime(o1);
        }
    });
    
    private static final TByteObjectHashMap<MarketEntryComparator> HELPER;
    public final byte idx;
    public final Comparator<MarketEntry> comparator;
    
    private MarketEntryComparator(final int idx, final Comparator<MarketEntry> comparator) {
        this.idx = (byte)idx;
        this.comparator = comparator;
    }
    
    public static MarketEntryComparator fromId(final byte idx) {
        return MarketEntryComparator.HELPER.get(idx);
    }
    
    static {
        HELPER = new TByteObjectHashMap<MarketEntryComparator>();
        final MarketEntryComparator[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final MarketEntryComparator value = values[i];
            MarketEntryComparator.HELPER.put(value.idx, value);
        }
    }
}
