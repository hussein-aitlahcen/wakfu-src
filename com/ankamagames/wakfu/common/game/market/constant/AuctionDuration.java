package com.ankamagames.wakfu.common.game.market.constant;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import org.jetbrains.annotations.*;

public enum AuctionDuration
{
    TWENTY_FOUR_HOURS(1, 86400000L, 0.3333333333333333), 
    FORTY_HEIGHT_HOURS(2, 172800000L, 0.5), 
    SEVENTY_TWO_HOURS(3, 259200000L, 1.0);
    
    public final byte id;
    public final long timeMs;
    public final GameInterval interval;
    public final double taxFactor;
    
    private AuctionDuration(final int idx, final long time, final double factor) {
        this.id = (byte)idx;
        this.timeMs = time;
        this.interval = new GameInterval(time / 1000L);
        this.taxFactor = factor;
    }
    
    @Nullable
    public static AuctionDuration fromId(final byte idx) {
        final AuctionDuration[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final AuctionDuration value = values[i];
            if (value.id == idx) {
                return value;
            }
        }
        return null;
    }
}
