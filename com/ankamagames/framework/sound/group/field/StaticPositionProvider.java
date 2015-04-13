package com.ankamagames.framework.sound.group.field;

import com.ankamagames.framework.sound.group.*;

public final class StaticPositionProvider implements ObservedSource
{
    private final float m_x;
    private final float m_y;
    private final float m_z;
    private final boolean m_isPositionRelative;
    private final int m_groupId;
    public static final double NUM_ELEVATION_IN_CUBE = 4.8;
    
    public StaticPositionProvider(final float x, final float y, final float z) {
        this(x, y, z, false, 0);
    }
    
    public StaticPositionProvider(final float x, final float y, final float z, final boolean positionRelative) {
        this(x, y, z, positionRelative, 0);
    }
    
    public StaticPositionProvider(final float x, final float y, final float z, final boolean positionRelative, final int groupId) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_isPositionRelative = positionRelative;
        this.m_groupId = groupId;
    }
    
    @Override
    public float getObservedX() {
        return this.m_x - this.m_y;
    }
    
    @Override
    public float getObservedY() {
        return -(this.m_x + this.m_y);
    }
    
    @Override
    public float getObservedZ() {
        return (float)(this.m_z / 4.8);
    }
    
    @Override
    public boolean isPositionRelative() {
        return this.m_isPositionRelative;
    }
    
    @Override
    public int getGroupInstanceId() {
        return this.m_groupId;
    }
}
