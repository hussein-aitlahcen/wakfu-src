package com.ankamagames.wakfu.common.game.protector;

public class TerritoryChaosHandler<T extends AbstractTerritory>
{
    protected boolean m_isInChaos;
    protected T m_territory;
    
    public TerritoryChaosHandler(final T territory) {
        super();
        this.m_territory = territory;
    }
    
    public void startChaos() {
        this.m_isInChaos = true;
    }
    
    public void endChaos() {
        this.m_isInChaos = false;
    }
    
    public boolean isInChaos() {
        return this.m_isInChaos;
    }
}
