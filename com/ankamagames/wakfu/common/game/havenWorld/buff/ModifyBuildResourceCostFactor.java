package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyBuildResourceCostFactor implements HavenWorldBuff
{
    private final int m_resourceCostFactor;
    
    public ModifyBuildResourceCostFactor(final ModifyBuildResourceCostFactorDefinition definition) {
        super();
        this.m_resourceCostFactor = definition.getResourcePercentModificator();
    }
    
    @Override
    public void apply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.modifyResourceCostFactor(this.m_resourceCostFactor);
    }
    
    @Override
    public void unapply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.modifyResourceCostFactor(-this.m_resourceCostFactor);
    }
    
    @Override
    public boolean onlyOnce() {
        return false;
    }
}
