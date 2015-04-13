package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;

public interface CharacterFightListener
{
    void onJoinFight(BasicCharacterInfo p0);
    
    void onLeaveFight(BasicCharacterInfo p0);
    
    void onWonFight(BasicCharacterInfo p0);
    
    void onLoseFight(BasicCharacterInfo p0);
}
