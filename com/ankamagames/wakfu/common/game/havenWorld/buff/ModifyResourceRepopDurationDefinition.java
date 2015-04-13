package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyResourceRepopDurationDefinition implements HavenWorldBuffDefinition
{
    private final int m_resoureType;
    private final float m_durationPercentModif;
    
    ModifyResourceRepopDurationDefinition(final int resoureType, final float durationPercentModif) {
        super();
        this.m_resoureType = resoureType;
        this.m_durationPercentModif = durationPercentModif;
    }
    
    public float getDurationPercentModif() {
        return this.m_durationPercentModif;
    }
    
    public int getResoureType() {
        return this.m_resoureType;
    }
}
