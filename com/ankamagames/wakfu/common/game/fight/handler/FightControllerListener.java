package com.ankamagames.wakfu.common.game.fight.handler;

import com.ankamagames.wakfu.common.datas.*;

public interface FightControllerListener
{
    void onControllerJoinFight(BasicCharacterInfo p0);
    
    void onControllerLoseFight(BasicCharacterInfo p0);
    
    void onControllerWinFight(BasicCharacterInfo p0);
}
