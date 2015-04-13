package com.ankamagames.wakfu.common.game.market;

import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.common.rawData.*;

public interface MarketEntry
{
    long getId();
    
    long getSellerId();
    
    String getSellerName();
    
    int getItemRefId();
    
    PackType getPackType();
    
    short getPackNumber();
    
    void decreasePackNumber(short p0);
    
    void increasePackNumber(short p0);
    
    int getPackPrice();
    
    AuctionDuration getDuration();
    
    long getReleaseDate();
    
    RawInventoryItem getRawItem();
    
    boolean isExpired(long p0);
    
    int compareReleaseDate(MarketEntry p0);
    
    int compareRemainingTime(MarketEntry p0);
    
    byte[] toRaw();
}
