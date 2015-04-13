package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;

public class AleaIsoCamera extends IsoCamera
{
    private int m_cameraMaskKey;
    private int m_groupId;
    private Lock m_lockMaskKey;
    
    public AleaIsoCamera(final AleaWorldScene scene) {
        super(scene);
        this.m_lockMaskKey = Lock.Unlocked;
    }
    
    public AleaIsoCamera(final AleaWorldScene scene, final float minZoom, final float maxZoom) {
        super(scene, minZoom, maxZoom);
        this.m_lockMaskKey = Lock.Unlocked;
    }
    
    public final int getMaskKey() {
        return this.m_cameraMaskKey;
    }
    
    public final int getGroupId() {
        return this.m_groupId;
    }
    
    public void lockMaskKey(final boolean lock) {
        this.m_lockMaskKey = (lock ? Lock.Prepare : Lock.Unlocked);
    }
    
    public boolean isLocked() {
        return this.m_lockMaskKey == Lock.Locked;
    }
    
    public final void setMaskKey(final int key, final int groupId) {
        if (this.m_lockMaskKey == Lock.Locked) {
            return;
        }
        this.m_cameraMaskKey = key;
        this.m_groupId = groupId;
        if (this.m_lockMaskKey == Lock.Prepare) {
            this.m_lockMaskKey = Lock.Locked;
        }
    }
    
    @Override
    public final float[] getLayerColor(final Maskable element) {
        return GroupLayerManager.getInstance().getLayerColor(element);
    }
    
    @Override
    public final int getGroupMaskKey() {
        final IsoWorldTarget target = this.getTrackingTarget();
        if (target instanceof Maskable) {
            final Maskable maskable = (Maskable)target;
            return maskable.getMaskKey();
        }
        return this.getMaskKey();
    }
    
    public static DisplayedScreenElement getDisplayedScreenElement(final DisplayedScreenWorld world, final int cameraX, final int cameraY, final int altitude) {
        return world.getNearesetElement(cameraX, cameraY, altitude, ElementFilter.NOT_EMPTY);
    }
    
    public final void resetMaskKey() {
        this.setMaskKey(0, 0);
    }
    
    private enum Lock
    {
        Prepare, 
        Locked, 
        Unlocked;
    }
}
