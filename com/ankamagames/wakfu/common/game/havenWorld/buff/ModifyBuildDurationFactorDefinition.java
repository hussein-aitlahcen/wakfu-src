package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyBuildDurationFactorDefinition implements HavenWorldBuffDefinition
{
    private final int m_durationPercentModificator;
    
    ModifyBuildDurationFactorDefinition(final int durationPercentModificator) {
        super();
        this.m_durationPercentModificator = durationPercentModificator;
    }
    
    public int getDurationPercentModificator() {
        return this.m_durationPercentModificator;
    }
}
