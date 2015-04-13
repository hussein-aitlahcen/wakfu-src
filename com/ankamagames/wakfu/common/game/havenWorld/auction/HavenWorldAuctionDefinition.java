package com.ankamagames.wakfu.common.game.havenWorld.auction;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class HavenWorldAuctionDefinition
{
    private final int m_havenWorldId;
    private final GameDateConst m_startDate;
    
    public HavenWorldAuctionDefinition(final int havenWorldId, final GameDateConst startDate) {
        super();
        this.m_havenWorldId = havenWorldId;
        this.m_startDate = startDate;
    }
    
    public int getHavenWorldId() {
        return this.m_havenWorldId;
    }
    
    public GameDateConst getStartDate() {
        return this.m_startDate;
    }
    
    public GameDateConst getEndDate() {
        final GameDate startDate = new GameDate(this.m_startDate);
        startDate.add(HavenWorldAuctionConstants.AUCTION_DURATION);
        return startDate;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HavenWorldAuctionDefinition{");
        sb.append("m_havenWorldId=").append(this.m_havenWorldId);
        sb.append(", m_startDate=").append(this.m_startDate);
        sb.append('}');
        return sb.toString();
    }
}
