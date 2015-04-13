package com.ankamagames.wakfu.client.core.world.river;

import com.ankamagames.framework.sound.group.*;

public class SoundZoneObservedSource implements ObservedSource
{
    private SoundZoneType m_type;
    
    public SoundZoneObservedSource(final SoundZoneType type) {
        super();
        this.m_type = type;
    }
    
    @Override
    public float getObservedX() {
        return SoundPartitionManager.INSTANCE.getX(this.m_type) - SoundPartitionManager.INSTANCE.getY(this.m_type);
    }
    
    @Override
    public float getObservedY() {
        return -(SoundPartitionManager.INSTANCE.getX(this.m_type) + SoundPartitionManager.INSTANCE.getY(this.m_type));
    }
    
    @Override
    public float getObservedZ() {
        return SoundPartitionManager.INSTANCE.getZ(this.m_type);
    }
    
    @Override
    public boolean isPositionRelative() {
        return false;
    }
    
    @Override
    public int getGroupInstanceId() {
        return 0;
    }
}
