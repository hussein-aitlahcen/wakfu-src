package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public interface FightEventListener
{
    void onControllerEvent(int p0, Object p1);
    
    void onSpecialFighterEvent(SpecialEvent p0);
    
    void addProperty(PropertyType p0);
    
    void removeProperty(PropertyType p0);
}
