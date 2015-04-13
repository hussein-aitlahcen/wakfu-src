package com.ankamagames.wakfu.common.game.hero;

import com.ankamagames.wakfu.common.datas.*;

public interface HeroesManagerListener<T extends BasicCharacterInfo>
{
    void heroAdded(T p0);
    
    void heroAddedToParty(T p0);
    
    void heroRemovedFromParty(T p0);
    
    void heroRemoved(T p0);
}
