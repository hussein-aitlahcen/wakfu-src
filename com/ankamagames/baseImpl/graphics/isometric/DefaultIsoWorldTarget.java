package com.ankamagames.baseImpl.graphics.isometric;

import java.util.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class DefaultIsoWorldTarget implements IsoWorldTarget, ScreenTarget
{
    private float m_worldX;
    private float m_worldY;
    private float m_altitude;
    private int m_screenX;
    private int m_screenY;
    private int m_screenHeight;
    private ArrayList<ScreenTargetWatcher> m_watchers;
    
    public DefaultIsoWorldTarget() {
        super();
        this.m_screenX = Integer.MIN_VALUE;
        this.m_screenY = Integer.MIN_VALUE;
    }
    
    public DefaultIsoWorldTarget(final float worldX, final float worldY, final float altitude) {
        super();
        this.m_screenX = Integer.MIN_VALUE;
        this.m_screenY = Integer.MIN_VALUE;
        this.m_worldX = worldX;
        this.m_worldY = worldY;
        this.m_altitude = altitude;
    }
    
    public DefaultIsoWorldTarget(final IsoWorldTarget target) {
        this(target.getWorldX(), target.getWorldY(), target.getAltitude());
    }
    
    @Override
    public float getEntityRenderRadius() {
        return 0.0f;
    }
    
    @Override
    public short getWorldCellAltitude() {
        return (short)MathHelper.fastFloor(this.m_altitude);
    }
    
    @Override
    public float getAltitude() {
        return this.m_altitude;
    }
    
    @Override
    public int getWorldCellX() {
        return MathHelper.fastRound(this.m_worldX);
    }
    
    @Override
    public int getWorldCellY() {
        return MathHelper.fastRound(this.m_worldY);
    }
    
    @Override
    public float getWorldX() {
        return this.m_worldX;
    }
    
    @Override
    public float getWorldY() {
        return this.m_worldY;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
        this.m_worldX = worldX;
        this.m_worldY = worldY;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float worldZ) {
        this.m_worldX = worldX;
        this.m_worldY = worldY;
        this.m_altitude = worldZ;
    }
    
    @Override
    public int getScreenX() {
        return this.m_screenX;
    }
    
    @Override
    public int getScreenY() {
        return this.m_screenY;
    }
    
    @Override
    public void setScreenX(final int x) {
        if (this.m_screenX == x) {
            return;
        }
        this.m_screenX = x;
        this.fireScreenPositionChanged();
    }
    
    @Override
    public void setScreenY(final int y) {
        if (this.m_screenY == y) {
            return;
        }
        this.m_screenY = y;
        this.fireScreenPositionChanged();
    }
    
    @Override
    public void setScreenTargetHeight(final int height) {
        if (this.m_screenHeight == height) {
            return;
        }
        this.m_screenHeight = height;
        this.fireScreenPositionChanged();
    }
    
    @Override
    public int getScreenTargetHeight() {
        return this.m_screenHeight;
    }
    
    @Override
    public boolean isPositionComputed() {
        return this.m_screenX != Integer.MIN_VALUE && this.m_screenY != Integer.MIN_VALUE;
    }
    
    @Override
    public void addWatcher(final ScreenTargetWatcher watcher) {
        if (this.m_watchers == null) {
            this.m_watchers = new ArrayList<ScreenTargetWatcher>(1);
        }
        this.m_watchers.add(watcher);
    }
    
    @Override
    public void removeWatcher(final ScreenTargetWatcher watcher) {
        if (this.m_watchers == null) {
            return;
        }
        this.m_watchers.remove(watcher);
        if (this.m_watchers.isEmpty()) {
            this.m_watchers = null;
            this.m_screenX = Integer.MIN_VALUE;
            this.m_screenY = Integer.MIN_VALUE;
        }
    }
    
    protected void fireScreenPositionChanged() {
        if (this.m_watchers != null) {
            for (int i = 0; i < this.m_watchers.size(); ++i) {
                this.m_watchers.get(i).screenTargetMoved(this, this.m_screenX, this.m_screenY, this.m_screenHeight);
            }
        }
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
}
