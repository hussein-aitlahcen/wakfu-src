package com.ankamagames.wakfu.common.game.fighter;

import gnu.trove.*;

public interface AggroUser
{
    long getAggroUserId();
    
    TLongShortHashMap getAggressiveList();
    
    void addAggro(AggroUser p0, short p1);
    
    void substractAggro(AggroUser p0, short p1);
    
    void setAggro(AggroUser p0, short p1);
    
    void removeAggroUser(AggroUser p0);
    
    void clearAggressiveList();
    
    String aggroToString();
}
