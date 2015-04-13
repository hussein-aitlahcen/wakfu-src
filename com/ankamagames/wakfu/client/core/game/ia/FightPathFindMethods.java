package com.ankamagames.wakfu.client.core.game.ia;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl.*;

public enum FightPathFindMethods
{
    TACKLE_AWARE(TackleAwareFightPathFindMethod.INSTANCE), 
    WITH_STEAMER_RAILS(SteamerRailsFightPathFindMethod.INSTANCE), 
    WITH_XELORS_DIAL(XelorsDialFightPathFindMethod.INSTANCE), 
    WITH_GATES(GateFightPathFindMethod.INSTANCE);
    
    private final FightPathFindMethod m_method;
    
    private FightPathFindMethods(final FightPathFindMethod method) {
        this.m_method = method;
    }
    
    public ClientPathFindResult findPath(final CharacterInfo currentFighter, final CharacterActor actor, final int availableMovementPoints) {
        return this.m_method.findPath(currentFighter, actor, availableMovementPoints);
    }
}
