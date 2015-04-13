package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class PerceptionBonusDefinition implements HavenWorldBuffDefinition
{
    private final int m_perceptionRate;
    
    public PerceptionBonusDefinition(final int perceptionRate) {
        super();
        this.m_perceptionRate = perceptionRate;
    }
    
    public int getPerceptionRate() {
        return this.m_perceptionRate;
    }
}
