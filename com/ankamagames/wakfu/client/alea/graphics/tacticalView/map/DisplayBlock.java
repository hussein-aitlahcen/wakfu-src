package com.ankamagames.wakfu.client.alea.graphics.tacticalView.map;

import com.ankamagames.baseImpl.graphics.alea.display.*;

public class DisplayBlock
{
    public final boolean walkable;
    public final boolean blockLos;
    public final float top;
    public final float leftHeight;
    public final float rightHeight;
    public final float m_centerX;
    public final float m_centerY;
    
    public DisplayBlock(final boolean walkable, final int top, final int leftHeight, final int rightHeight, final int x, final int y, final boolean blockLos) {
        super();
        this.walkable = walkable;
        this.top = top;
        this.blockLos = blockLos;
        this.leftHeight = leftHeight * 10;
        this.rightHeight = rightHeight * 10;
        this.m_centerX = ScreenWorld.isoToScreenXf(x, y);
        this.m_centerY = ScreenWorld.isoToScreenYf(x, y, top);
    }
    
    public float getCenterX() {
        return this.m_centerX;
    }
    
    public float getCenterY() {
        return this.m_centerY;
    }
    
    public float getLeft() {
        return this.m_centerX - 43.0f;
    }
    
    public float getRight() {
        return this.m_centerX + 43.0f;
    }
    
    public float getCenterTop() {
        return this.m_centerY + 21.5f;
    }
    
    public float getCenterBottom() {
        return this.m_centerY - 21.5f;
    }
}
