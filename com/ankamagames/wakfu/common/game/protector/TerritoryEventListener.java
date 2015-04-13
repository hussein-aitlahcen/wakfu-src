package com.ankamagames.wakfu.common.game.protector;

import com.ankamagames.wakfu.common.datas.*;

public interface TerritoryEventListener
{
    void onPlayerEnterTerritory(BasicCharacterInfo p0, AbstractTerritory p1);
    
    void onPlayerLeaveTerritory(BasicCharacterInfo p0, AbstractTerritory p1);
}
