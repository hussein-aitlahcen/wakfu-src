package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class ModifyBuildDurationFactors implements HavenWorldBuff
{
    private final int m_durationPercentModificator;
    
    public ModifyBuildDurationFactors(final ModifyBuildDurationFactorDefinition definition) {
        super();
        this.m_durationPercentModificator = definition.getDurationPercentModificator();
    }
    
    @Override
    public void apply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.modifyBuildDurationFactor(this.m_durationPercentModificator);
    }
    
    @Override
    public void unapply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.modifyBuildDurationFactor(-this.m_durationPercentModificator);
    }
    
    @Override
    public boolean onlyOnce() {
        return false;
    }
}
