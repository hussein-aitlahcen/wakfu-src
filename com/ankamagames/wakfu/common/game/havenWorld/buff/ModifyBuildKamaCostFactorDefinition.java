package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyBuildKamaCostFactorDefinition implements HavenWorldBuffDefinition
{
    private final int m_kamaPercentModificator;
    
    ModifyBuildKamaCostFactorDefinition(final int kamaPercentModificator) {
        super();
        this.m_kamaPercentModificator = kamaPercentModificator;
    }
    
    public int getKamaPercentModificator() {
        return this.m_kamaPercentModificator;
    }
}
