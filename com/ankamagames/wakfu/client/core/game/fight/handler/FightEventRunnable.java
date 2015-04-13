package com.ankamagames.wakfu.client.core.game.fight.handler;

import com.ankamagames.wakfu.common.datas.*;

public interface FightEventRunnable
{
    void runSpellCastEvent(BasicCharacterInfo p0, long p1);
    
    void runFighterEvent(BasicCharacterInfo p0);
    
    void runFightEvent();
}
