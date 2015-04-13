package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import gnu.trove.*;

public abstract class AbstractEffectManager<FX extends Effect>
{
    private final TIntObjectHashMap<FX> m_effects;
    
    protected AbstractEffectManager() {
        super();
        this.m_effects = new TIntObjectHashMap<FX>();
    }
    
    public void addEffect(final FX e) {
        this.m_effects.put(e.getEffectId(), e);
    }
    
    public FX getEffect(final int effectId) {
        return this.m_effects.get(effectId);
    }
}
