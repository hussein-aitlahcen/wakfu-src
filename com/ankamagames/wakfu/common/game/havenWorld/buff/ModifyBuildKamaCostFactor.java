package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyBuildKamaCostFactor implements HavenWorldBuff
{
    private final int m_kamaCostFactor;
    
    public ModifyBuildKamaCostFactor(final ModifyBuildKamaCostFactorDefinition definition) {
        super();
        this.m_kamaCostFactor = definition.getKamaPercentModificator();
    }
    
    @Override
    public void apply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.modifyKamaCostFactor(this.m_kamaCostFactor);
    }
    
    @Override
    public void unapply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.modifyKamaCostFactor(-this.m_kamaCostFactor);
    }
    
    @Override
    public boolean onlyOnce() {
        return false;
    }
}
