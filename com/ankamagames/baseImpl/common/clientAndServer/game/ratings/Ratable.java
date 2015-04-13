package com.ankamagames.baseImpl.common.clientAndServer.game.ratings;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface Ratable<R extends Rank> extends Comparable<Ratable<R>>
{
    long getId();
    
    int getStrength();
    
    void setStrength(int p0);
    
    R getRank();
    
    void setRank(R p0);
    
    int getRanking();
    
    void setRanking(int p0);
    
    GameDateConst getLastModification();
}
