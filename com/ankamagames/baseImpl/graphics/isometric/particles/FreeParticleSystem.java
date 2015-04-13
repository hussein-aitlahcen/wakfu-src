package com.ankamagames.baseImpl.graphics.isometric.particles;

import java.util.*;
import com.ankamagames.baseImpl.graphics.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class FreeParticleSystem extends IsoParticleSystem implements IsoWorldTarget, ScreenTarget
{
    private IsoWorldTarget m_target;
    private int m_fightId;
    private int m_screenX;
    private int m_screenY;
    private int m_screenHeight;
    private ArrayList<ScreenTargetWatcher> m_watchers;
    
    public FreeParticleSystem(final boolean isEditable) {
        super(isEditable);
        this.m_fightId = -1;
        this.m_screenX = Integer.MIN_VALUE;
        this.m_screenY = Integer.MIN_VALUE;
    }
    
    @Override
    protected boolean playSound() {
        return this.playSound(this.getFightId());
    }
    
    @Override
    public float getX() {
        if (this.m_target != null) {
            return this.m_target.getWorldX();
        }
        return super.getX();
    }
    
    @Override
    public float getY() {
        if (this.m_target != null) {
            return this.m_target.getWorldY();
        }
        return super.getY();
    }
    
    @Override
    public float getZ() {
        if (this.m_target != null) {
            return this.m_target.getAltitude();
        }
        return super.getZ();
    }
    
    public IsoWorldTarget getTarget() {
        return this.m_target;
    }
    
    public void setTarget(@Nullable final IsoWorldTarget target) {
        if (target == this.m_target) {
            return;
        }
        if (this.m_target instanceof ParticleTarget) {
            ((ParticleTarget)this.m_target).onParticleDetached(this);
        }
        this.m_target = target;
        if (this.m_target instanceof ParticleTarget) {
            ((ParticleTarget)this.m_target).onParticleAttached(this);
        }
    }
    
    @Override
    public boolean isVisible() {
        final boolean visible = super.isVisible();
        if (this.m_target != null) {
            return visible && this.m_target.isVisible();
        }
        return visible;
    }
    
    public void setTarget(final AnimatedElement target, final float heightPercent, final int zOffset) {
        this.setTarget((heightPercent == 0.0f && zOffset == 0) ? target : new AnimatedElementWithOffset(target, heightPercent, zOffset));
    }
    
    @Override
    public float getAltitude() {
        return this.getZ();
    }
    
    @Override
    public float getWorldX() {
        return this.getX();
    }
    
    @Override
    public float getWorldY() {
        return this.getY();
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
        this.setPosition(worldX, worldY);
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float worldZ) {
        this.setPosition(worldX, worldY, worldZ);
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
        return this.m_screenX != Integer.MIN_VALUE && this.m_screenY == Integer.MIN_VALUE;
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
            final int n = Integer.MIN_VALUE;
            this.m_screenY = n;
            this.m_screenX = n;
        }
    }
    
    protected void fireScreenPositionChanged() {
        if (this.m_watchers != null) {
            for (int numElements = this.m_watchers.size(), i = 0; i < numElements; ++i) {
                this.m_watchers.get(i).screenTargetMoved(this, this.m_screenX, this.m_screenY, this.m_screenHeight);
            }
        }
    }
    
    public int getFightId() {
        IsoWorldTarget target = this.m_target;
        if (target instanceof AnimatedElementWithOffset) {
            target = ((AnimatedElementWithOffset)target).getTarget();
        }
        if (target instanceof AnimatedObject) {
            return ((AnimatedObject)target).getCurrentFightId();
        }
        return this.m_fightId;
    }
    
    public void setFightId(final int fightId) {
        this.m_fightId = fightId;
    }
    
    @Override
    public float getEntityRenderRadius() {
        return this.m_renderRadius;
    }
    
    @Override
    public boolean isMaskUndefined() {
        return this.m_target != null || super.isMaskUndefined();
    }
    
    @Override
    public int getDeltaZ() {
        if (this.m_target instanceof AnimatedElement) {
            return ((AnimatedElement)this.m_target).getDeltaZ() + (this.m_behindMobile ? -1 : 1);
        }
        return super.getDeltaZ();
    }
    
    @Override
    public boolean computeZOrderAndMaskKey(final IsoWorldScene scene) {
        if (this.m_target != null) {
            IsoWorldTarget target = this.m_target;
            if (target instanceof AnimatedElementWithOffset) {
                target = ((AnimatedElementWithOffset)target).getTarget();
            }
            if (target instanceof AnimatedElement) {
                final AnimatedElement ae = (AnimatedElement)target;
                final Entity entity = ae.getEntity();
                if (entity != null) {
                    this.m_zOrder = entity.m_zOrder + (this.m_behindMobile ? -1 : 1);
                    if (this.m_zOrder < 0L) {
                        this.m_zOrder = 0L;
                    }
                    this.setMaskKey(ae.getMaskKey(), ae.getLayerId());
                    return true;
                }
            }
        }
        return super.computeZOrderAndMaskKey(scene);
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        this.m_fightId = -1;
        this.m_screenX = Integer.MIN_VALUE;
        this.m_screenY = Integer.MIN_VALUE;
        this.m_screenHeight = 0;
        this.m_watchers = null;
    }
    
    @Override
    protected void checkin() {
        super.checkin();
        this.setTarget(null);
    }
    
    @Override
    public String toString() {
        return "FreeParticleSystem{m_target=" + this.m_target + ", m_fightId=" + this.m_fightId + ", m_screenX=" + this.m_screenX + ", m_screenY=" + this.m_screenY + ", m_screenHeight=" + this.m_screenHeight + ", m_watchers=" + this.m_watchers + '}';
    }
}
