package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.ai.dataProvider.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public interface EffectContext<FX extends Effect>
{
    AbstractEffectManager<FX> getEffectManager();
    
    EffectExecutionListener getEffectExecutionListener();
    
    TargetInformationProvider<EffectUser> getTargetInformationProvider();
    
    TurnBasedTimeline getTimeline();
    
    FightMap getFightMap();
    
    EffectUserInformationProvider getEffectUserInformationProvider();
    
    BasicEffectAreaManager getEffectAreaManager();
    
    byte getContextType();
    
    SpellCaster getSpellCaster();
    
    MonsterSpawner getMonsterSpawner();
}
