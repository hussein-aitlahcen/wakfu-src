package com.ankamagames.wakfu.client.core.havenWorld;

import com.ankamagames.framework.kernel.core.maths.*;

public class HavenWorldBuildingVisualElement
{
    private final int m_gfxId;
    private final String m_animationName;
    private final boolean m_hasGuildColor;
    private final Point3 m_relativePosition;
    private final Direction8 m_direction;
    private final boolean m_occluder;
    private final byte m_height;
    
    public HavenWorldBuildingVisualElement(final int gfxId, final String animationName, final boolean hasGuildColor, final boolean occluder, final byte height, final Point3 relativePosition, final Direction8 direction) {
        super();
        this.m_gfxId = gfxId;
        this.m_animationName = animationName;
        this.m_hasGuildColor = hasGuildColor;
        this.m_relativePosition = relativePosition;
        this.m_occluder = occluder;
        this.m_height = height;
        this.m_direction = direction;
    }
    
    public boolean isHasGuildColor() {
        return this.m_hasGuildColor;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public String getAnimationName() {
        return this.m_animationName;
    }
    
    public Point3 getRelativePosition() {
        return this.m_relativePosition;
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    public boolean isOccluder() {
        return this.m_occluder;
    }
    
    public byte getHeight() {
        return this.m_height;
    }
}
