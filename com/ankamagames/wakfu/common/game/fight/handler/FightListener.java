package com.ankamagames.wakfu.common.game.fight.handler;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public interface FightListener<C extends BasicCharacterInfo>
{
    void onPlacementStart();
    
    void onPlacementEnd();
    
    void onFightStart();
    
    void onFightEnd();
    
    void onTableTurnStart();
    
    void onTableTurnEnd();
    
    void onFighterStartTurn(C p0);
    
    void onFighterEndTurn(C p0);
    
    void onFighterJoinFight(C p0);
    
    void onFighterOutOfPlay(C p0);
    
    void onFighterWinFight(C p0);
    
    void onFighterLoseFight(C p0);
    
    void onFighterCastSpell(C p0, AbstractSpell p1);
    
    void onEffectAreaGoesOffPlay(AbstractEffectArea p0);
    
    void onFighterRemovedFromFight(C p0);
    
    void onFightEnded();
}
