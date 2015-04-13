package com.ankamagames.xulor2.util;

public class DisplayableMapPointIcon
{
    private final String m_texturePath;
    private final float m_xHotPoint;
    private final float m_yHotPoint;
    
    public DisplayableMapPointIcon(final String texturePath, final float xHotPoint, final float yHotPoint) {
        super();
        this.m_texturePath = texturePath;
        this.m_xHotPoint = xHotPoint;
        this.m_yHotPoint = yHotPoint;
    }
    
    public String getTexturePath() {
        return this.m_texturePath;
    }
    
    public float getxHotPoint() {
        return this.m_xHotPoint;
    }
    
    public float getyHotPoint() {
        return this.m_yHotPoint;
    }
}
