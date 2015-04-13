package com.ankamagames.wakfu.common.game.havenWorld.buff;

public final class PerceptionBonus implements HavenWorldBuff
{
    private final int m_perceptionRate;
    
    public PerceptionBonus(final PerceptionBonusDefinition definition) {
        super();
        this.m_perceptionRate = definition.getPerceptionRate();
    }
    
    @Override
    public void apply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.setPerceptionRate(this.m_perceptionRate);
    }
    
    @Override
    public void unapply(final short worldId, final long buildingUid) {
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        worldZoneBuffs.setPerceptionRate(0);
    }
    
    @Override
    public boolean onlyOnce() {
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PerceptionBonus{");
        sb.append("m_perceptionRate=").append(this.m_perceptionRate);
        sb.append('}');
        return sb.toString();
    }
}
