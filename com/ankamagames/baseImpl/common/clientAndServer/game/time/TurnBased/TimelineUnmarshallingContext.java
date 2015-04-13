package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public class TimelineUnmarshallingContext
{
    private final FighterSortingStrategy m_fighterSortingStrategy;
    private final EffectContext<?> m_effectContext;
    
    public FighterSortingStrategy getFighterSortingStrategy() {
        return this.m_fighterSortingStrategy;
    }
    
    public TimelineUnmarshallingContext(final EffectContext effectContext, final FighterSortingStrategy fighterSortingStrategy) {
        super();
        if (effectContext == null || fighterSortingStrategy == null) {
            throw new IllegalArgumentException("aucun argument du constructeur de " + this.getClass().getSimpleName() + " ne doit \u00eatre null");
        }
        this.m_effectContext = (EffectContext<?>)effectContext;
        this.m_fighterSortingStrategy = fighterSortingStrategy;
    }
    
    public BasicEffectArea getArea(final long id) {
        final BasicEffectAreaManager effectAreaManager = this.m_effectContext.getEffectAreaManager();
        return (effectAreaManager == null) ? null : effectAreaManager.getEffectAreaWithId(id);
    }
    
    public RunningEffect getRunningEffect(final long id) {
        if (this.m_effectContext.getEffectUserInformationProvider() == null) {
            return null;
        }
        final Iterator<? extends EffectUser> iteu = this.m_effectContext.getTargetInformationProvider().getAllPossibleTargets();
        while (iteu.hasNext()) {
            final EffectUser effectUser = (EffectUser)iteu.next();
            if (effectUser.getRunningEffectManager() == null) {
                continue;
            }
            final RunningEffect re = effectUser.getRunningEffectManager().getRunningEffectFromUID(id);
            if (re != null) {
                return re;
            }
        }
        return null;
    }
}
