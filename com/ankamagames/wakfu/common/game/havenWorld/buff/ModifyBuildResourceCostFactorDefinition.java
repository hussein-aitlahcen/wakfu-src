package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyBuildResourceCostFactorDefinition implements HavenWorldBuffDefinition
{
    private final int m_resourcePercentModificator;
    
    ModifyBuildResourceCostFactorDefinition(final int resourcePercentModificator) {
        super();
        this.m_resourcePercentModificator = resourcePercentModificator;
    }
    
    public int getResourcePercentModificator() {
        return this.m_resourcePercentModificator;
    }
}
