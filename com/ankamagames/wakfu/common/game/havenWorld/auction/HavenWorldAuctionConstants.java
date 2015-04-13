package com.ankamagames.wakfu.common.game.havenWorld.auction;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class HavenWorldAuctionConstants
{
    public static final int GUILD_LEVEL_MIN_REQUIRED_FOR_AUCTION = 3;
    public static final int START_BID_PRICE = 100;
    public static final GameIntervalConst AUCTION_DURATION;
    public static final int BID_INCREASE_BY_STEP = 100;
    public static final int BID_INCREASE_STEP = 1000;
    public static final int SECONDS_LEFT_TO_POSTPONE_AUCTION = 30;
    public static final int HAVEN_WORLD_AUCTION_BOOK_ID = 17426;
    
    static {
        AUCTION_DURATION = GameIntervalConst.WEEK_INTERVAL;
    }
}
