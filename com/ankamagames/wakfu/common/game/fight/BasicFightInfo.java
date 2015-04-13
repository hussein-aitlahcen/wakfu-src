package com.ankamagames.wakfu.common.game.fight;

import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public interface BasicFightInfo<F extends BasicFighter> extends FightersInformationProvider<F>
{
    int getId();
    
    @NotNull
    FightModel getModel();
    
    AbstractFight.FightStatus getStatus();
    
    JoinFightResult canJoinTeam(F p0, byte p1);
    
    byte getTeamId(long p0);
    
    byte getInitiatingTeamId();
    
    FightMap getFightMap();
    
    EffectContext getContext();
    
    BasicEffectAreaManager getEffectAreaManager();
}
