package com.ankamagames.wakfu.common.game.item.rent;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public interface RentInfo
{
    int getType();
    
    void setInitialDuration(long p0);
    
    boolean isExpired();
    
    void toRaw(RawRentInfo p0);
    
    void fromRaw(RawRentInfo p0);
    
    RentInfo getCopy();
    
    void addDuration(long p0);
}
