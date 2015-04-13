package com.ankamagames.baseImpl.graphics.isometric;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class AnimatedElementWithOffset implements IsoWorldTarget, ParticleTarget
{
    private AnimatedElement m_target;
    private float m_heightPercent;
    private int m_offset;
    
    public AnimatedElementWithOffset(@NotNull final AnimatedElement target, final float heightPercent, final int offset) {
        super();
        this.m_target = target;
        this.m_heightPercent = heightPercent;
        this.m_offset = offset;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
        this.m_target.setWorldPosition(worldX, worldY);
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float worldZ) {
        this.m_target.setWorldPosition(worldX, worldY, worldZ);
    }
    
    @Override
    public float getEntityRenderRadius() {
        return this.m_target.getEntityRenderRadius();
    }
    
    @Override
    public float getWorldX() {
        return this.m_target.getWorldX();
    }
    
    @Override
    public float getWorldY() {
        return this.m_target.getWorldY();
    }
    
    @Override
    public float getAltitude() {
        return this.m_target.getAltitude() + this.getOffset();
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_target.getWorldCellX();
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_target.getWorldCellY();
    }
    
    @Override
    public short getWorldCellAltitude() {
        return (short)(this.m_target.getWorldCellAltitude() + this.getOffset());
    }
    
    public IsoWorldTarget getTarget() {
        return this.m_target;
    }
    
    public void setTarget(@NotNull final AnimatedElement target) {
        this.m_target = target;
    }
    
    @Override
    public boolean isVisible() {
        return this.m_target.isVisible();
    }
    
    @Override
    public void onParticleAttached(final FreeParticleSystem system) {
        this.m_target.onParticleAttached(system);
    }
    
    @Override
    public void onParticleDetached(final FreeParticleSystem system) {
        this.m_target.onParticleDetached(system);
    }
    
    @Override
    public void onDestroy() {
    }
    
    private float getOffset() {
        return this.m_target.getVisualHeight() * this.m_heightPercent + this.m_offset;
    }
}
