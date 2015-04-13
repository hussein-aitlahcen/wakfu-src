package com.ankamagames.wakfu.client.alea.graphics.fightView;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

interface FightViewRules
{
    void onFighterJoinFight(CharacterInfo p0);
    
    void onFighterLeaveFight(CharacterInfo p0);
    
    void onBasicEffectAreaAdded(FightInfo p0, BasicEffectArea p1);
    
    void onBasicEffectAreaTeleported(FightInfo p0, BasicEffectArea p1);
    
    void onBasicEffectAreaRemoved(FightInfo p0, BasicEffectArea p1);
    
    boolean isParticuleVisibleForFight();
    
    boolean canDisplayFlyingValue();
    
    void onExternalFightCreation(ExternalFightInfo p0);
    
    void updateFightVisibility(FightInfo p0);
}
