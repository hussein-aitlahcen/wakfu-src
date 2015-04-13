package com.ankamagames.wakfu.common.game.market;

public interface MarketObserver
{
    void entryAdded(Market p0, MarketEntry p1);
    
    void entryRemoved(Market p0, MarketEntry p1);
    
    void entryPackPurchased(Market p0, MarketEntry p1, short p2);
    
    void entriesSalesFetched(Market p0, long p1);
    
    void entryOutdated(Market p0, MarketEntry p1);
    
    void entryOutdatedFetched(Market p0, long p1, MarketEntry p2);
}
