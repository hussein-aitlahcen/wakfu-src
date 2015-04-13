package com.ankamagames.baseImpl.graphics.alea.ambiance;

import java.util.*;

public final class Ambiance
{
    private final int m_id;
    private final ArrayList<EffectContext> m_effects;
    
    public Ambiance() {
        this(0);
    }
    
    public Ambiance(final int ambianceId) {
        super();
        this.m_effects = new ArrayList<EffectContext>();
        this.m_id = ambianceId;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public final void addEffect(final Effect effect) {
        if (effect != null) {
            this.m_effects.add(new EffectContext(effect, this.m_id));
        }
    }
    
    ArrayList<EffectContext> getEffects() {
        return this.m_effects;
    }
    
    public final int getEffectCount() {
        return this.m_effects.size();
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder("[ Ambiance : ").append(this.getEffectCount()).append(" effect(s)");
        return s.toString();
    }
    
    public void update(final int deltaTime) {
        for (int i = 0; i < this.m_effects.size(); ++i) {
            this.m_effects.get(i).update(deltaTime);
        }
    }
}
