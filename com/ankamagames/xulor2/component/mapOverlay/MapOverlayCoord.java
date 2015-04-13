package com.ankamagames.xulor2.component.mapOverlay;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class MapOverlayCoord
{
    private static final Logger m_logger;
    public static final int OFFSET_PADDING = 20;
    private float m_isoCenterX;
    private float m_isoCenterY;
    private float m_isoCenterZ;
    private float m_desiredIsoCenterX;
    private float m_desiredIsoCenterY;
    private float m_desiredIsoCenterZ;
    private int m_cellNumber;
    private float m_zoom;
    
    public MapOverlayCoord() {
        super();
        this.m_isoCenterX = Float.NaN;
        this.m_isoCenterY = Float.NaN;
        this.m_isoCenterZ = Float.NaN;
        this.m_desiredIsoCenterX = Float.NaN;
        this.m_desiredIsoCenterY = Float.NaN;
        this.m_desiredIsoCenterZ = Float.NaN;
        this.m_cellNumber = 1000;
    }
    
    public void setCellNumber(final int cellNumber) {
        this.m_cellNumber = cellNumber;
    }
    
    public void setZoom(final float zoom) {
        this.m_zoom = zoom;
    }
    
    public float getIsoCenterX() {
        return this.m_isoCenterX;
    }
    
    public float getIsoCenterY() {
        return this.m_isoCenterY;
    }
    
    public float getIsoCenterZ() {
        return this.m_isoCenterZ;
    }
    
    public void setIsoCenterX(final float isoCenterX) {
        if (Float.isNaN(this.m_desiredIsoCenterX)) {
            this.m_isoCenterX = isoCenterX;
            this.m_desiredIsoCenterX = isoCenterX;
        }
        else {
            this.m_desiredIsoCenterX = isoCenterX;
        }
        assert !Float.isNaN(this.m_isoCenterX);
    }
    
    public void setIsoCenterY(final float isoCenterY) {
        if (Float.isNaN(this.m_desiredIsoCenterY)) {
            this.m_isoCenterY = isoCenterY;
            this.m_desiredIsoCenterY = isoCenterY;
        }
        else {
            this.m_desiredIsoCenterY = isoCenterY;
        }
        assert !Float.isNaN(this.m_isoCenterY);
    }
    
    public void setIsoCenterZ(final float isoCenterZ) {
        if (Float.isNaN(this.m_desiredIsoCenterZ)) {
            this.m_isoCenterZ = isoCenterZ;
            this.m_desiredIsoCenterZ = isoCenterZ;
        }
        else {
            this.m_desiredIsoCenterZ = isoCenterZ;
        }
        assert !Float.isNaN(this.m_isoCenterZ);
    }
    
    public void reset() {
        this.m_isoCenterX = 0.0f;
        this.m_isoCenterY = 0.0f;
        this.m_isoCenterZ = 0.0f;
    }
    
    public float getCenterScreenX(final IsoWorldScene scene) {
        return scene.isoToScreenX(this.m_isoCenterX, this.m_isoCenterY);
    }
    
    public float getCenterScreenY(final IsoWorldScene scene) {
        return scene.isoToScreenY(this.m_isoCenterX, this.m_isoCenterY, this.m_isoCenterZ);
    }
    
    public int getMapCenterScreenX(final IsoWorldScene scene) {
        return (int)scene.isoToScreenX(this.m_desiredIsoCenterX, this.m_desiredIsoCenterY);
    }
    
    public int getMapCenterScreenY(final IsoWorldScene scene) {
        return (int)scene.isoToScreenY(this.m_desiredIsoCenterX, this.m_desiredIsoCenterY);
    }
    
    public Point2 getMouseToIso(final IsoWorldScene scene, final int x, final int y) {
        final float dx = scene.screenToIsoX(x, y) + this.m_isoCenterX;
        final float dy = scene.screenToIsoY(x, y) + this.m_isoCenterY;
        return new Point2(dx, dy);
    }
    
    public Point2 isoToScreen(final IsoWorldScene scene, final int isoX, final int isoY, final boolean useDesiredIsoCenter) {
        final float x = isoX - (useDesiredIsoCenter ? this.m_desiredIsoCenterX : this.m_isoCenterX);
        final float y = isoY - (useDesiredIsoCenter ? this.m_desiredIsoCenterY : this.m_isoCenterY);
        final float dx = scene.isoToScreenX(x, y);
        final float dy = scene.isoToScreenY(x, y);
        return new Point2(dx, dy);
    }
    
    public Point2 computeCenterWithPrevious(final IsoWorldScene scene) {
        final float previousIsoCenterX = this.m_isoCenterX;
        final float previousIsoCenterY = this.m_isoCenterY;
        final float previousIsoCenterZ = this.m_isoCenterZ;
        this.compute(true);
        final float dx = this.m_isoCenterX - previousIsoCenterX;
        final float dy = this.m_isoCenterY - previousIsoCenterY;
        final float x = scene.isoToScreenX(dx, dy);
        final float y = scene.isoToScreenY(dx, dy, this.m_isoCenterZ - previousIsoCenterZ);
        return new Point2(x, y);
    }
    
    public Point2 computeCenter(final IsoWorldScene scene, final boolean dontInterpolateNextRender) {
        this.compute(dontInterpolateNextRender);
        final float x = this.getCenterScreenX(scene);
        final float y = this.getCenterScreenY(scene);
        return new Point2(x, y);
    }
    
    public float getIsoValue(final float iso, final float desiredIso, final boolean dontInterpolate) {
        return dontInterpolate ? desiredIso : this.pointCompute(iso, desiredIso);
    }
    
    private void compute(final boolean dontInterpolateNextRender) {
        this.m_isoCenterX = this.getIsoValue(this.m_isoCenterX, this.m_desiredIsoCenterX, dontInterpolateNextRender);
        this.m_isoCenterY = this.getIsoValue(this.m_isoCenterY, this.m_desiredIsoCenterY, dontInterpolateNextRender);
        this.m_isoCenterZ = this.getIsoValue(this.m_isoCenterZ, this.m_desiredIsoCenterZ, dontInterpolateNextRender);
    }
    
    private float pointCompute(final float position, final float desiredPosition) {
        assert !Float.isNaN(position) : "position is NaN";
        assert !Float.isNaN(desiredPosition) : "BadMoFo !!!";
        if (position == desiredPosition) {
            return position;
        }
        final float delta = desiredPosition - position;
        final float d = Math.abs(delta);
        if (d > this.m_cellNumber) {
            return desiredPosition;
        }
        if (d > this.m_zoom) {
            return position + delta / 20.0f;
        }
        return position;
    }
    
    public boolean isInitialized() {
        return !Float.isNaN(this.m_isoCenterX) && !Float.isNaN(this.m_isoCenterY) && !Float.isNaN(this.m_isoCenterZ) && !Float.isNaN(this.m_desiredIsoCenterX) && !Float.isNaN(this.m_desiredIsoCenterY) && !Float.isNaN(this.m_desiredIsoCenterZ);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapOverlayCoord.class);
    }
}
