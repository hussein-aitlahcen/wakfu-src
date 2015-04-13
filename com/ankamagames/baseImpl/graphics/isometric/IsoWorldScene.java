package com.ankamagames.baseImpl.graphics.isometric;

import com.ankamagames.framework.graphics.opengl.base.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class IsoWorldScene extends Scene
{
    public static final float DEFAULT_CELL_WIDTH = 86.0f;
    public static final float DEFAULT_CELL_HEIGHT = 43.0f;
    public static final float DEFAULT_ELEVATION_UNIT = 10.0f;
    public static final float NUM_ELEVATION_IN_CUBE = 4.8f;
    public static float DEFAULT_MIN_ZOOM_FACTOR;
    public static float DEFAULT_DEFAULT_ZOOM_FACTOR;
    public static float DEFAULT_MAX_ZOOM_FACTOR;
    protected float m_minZoom;
    protected float m_maxZoom;
    protected float m_cellWidth;
    protected float m_cellHeight;
    protected float m_elevationUnit;
    protected final ArrayList<Entity> m_sortedEntities;
    protected final ArrayList<Entity> m_unsortedEntities;
    protected final TLongArrayList m_zList;
    protected final ArrayList<Entity> m_entityList;
    protected final AbstractCamera m_isoCamera;
    private SceneEntityFilter m_entityFilter;
    
    public IsoWorldScene(final float minZoom, final float maxZoom) {
        super();
        this.m_cellWidth = 86.0f;
        this.m_cellHeight = 43.0f;
        this.m_elevationUnit = 10.0f;
        this.m_entityFilter = SceneEntityFilter.DEFAULT;
        this.m_minZoom = minZoom;
        this.m_maxZoom = maxZoom;
        this.m_isoCamera = this.createCamera();
        this.m_sortedEntities = new ArrayList<Entity>(2048);
        this.m_zList = new TLongArrayList(2048);
        this.m_unsortedEntities = new ArrayList<Entity>(1024);
        this.m_entityList = new ArrayList<Entity>(3072);
    }
    
    public void setEntityFilter(@NotNull final SceneEntityFilter entityFilter) {
        this.m_entityFilter.clear();
        this.m_entityFilter = entityFilter;
    }
    
    public void addEntity(final Entity entity, final boolean needSorting) {
        if (!this.m_entityFilter.acceptEntity(entity, needSorting)) {
            return;
        }
        this.forceAddEntity(entity, needSorting);
    }
    
    public void forceAddEntity(final Entity entity, final boolean needSorting) {
        if (needSorting) {
            this.m_sortedEntities.add(entity);
        }
        else {
            this.m_unsortedEntities.add(entity);
        }
    }
    
    public final void removeEntity(final Entity entity, final boolean isSorted) {
        if (isSorted) {
            this.m_sortedEntities.remove(entity);
        }
        else {
            this.m_unsortedEntities.remove(entity);
        }
    }
    
    public float getCellWidth() {
        return this.m_cellWidth;
    }
    
    public void setCellWidth(final float cellWidth) {
        this.m_cellWidth = cellWidth;
    }
    
    public float getCellHeight() {
        return this.m_cellHeight;
    }
    
    public void setCellHeight(final float cellHeight) {
        this.m_cellHeight = cellHeight;
    }
    
    public float getElevationUnit() {
        return this.m_elevationUnit;
    }
    
    public void setElevationUnit(final float elevationUnit) {
        this.m_elevationUnit = elevationUnit;
    }
    
    public final AbstractCamera getCam() {
        return this.m_isoCamera;
    }
    
    public IsoCamera getIsoCamera() {
        return (IsoCamera)this.m_isoCamera;
    }
    
    public IsoWorldTarget getCameraTarget() {
        return this.getIsoCamera().getTrackingTarget();
    }
    
    public void setCameraTarget(final IsoWorldTarget cameraTarget) {
        this.getIsoCamera().setTrackingTarget(cameraTarget);
    }
    
    public void alignCameraOnTrackingTarget() {
        this.getIsoCamera().alignOnTrackingTarget();
    }
    
    public float getDesiredZoomFactor() {
        if (this.m_isoCamera != null) {
            return this.getIsoCamera().getDesiredZoomFactor();
        }
        return 1.0f;
    }
    
    public void setDesiredZoomFactor(final float desiredZoomFactor) {
        if (this.m_isoCamera != null) {
            this.getIsoCamera().setDesiredZoomFactor(desiredZoomFactor);
        }
    }
    
    protected AbstractCamera createCamera() {
        return new IsoCamera(this, this.m_minZoom, this.m_maxZoom);
    }
    
    public float isoToScreenX(final float isoLocalX, final float isoLocalY) {
        return (isoLocalX - isoLocalY) * this.m_cellWidth * 0.5f;
    }
    
    public float isoToScreenY(final float isoLocalX, final float isoLocalY) {
        return -(isoLocalX + isoLocalY) * this.m_cellHeight * 0.5f;
    }
    
    public float isoToScreenY(final float isoLocalX, final float isoLocalY, final float isoLocalZ) {
        return this.isoToScreenY(isoLocalX, isoLocalY) + isoLocalZ * this.m_elevationUnit;
    }
    
    public float screenToIsoX(final float screenX, final float screenY) {
        return screenX / this.m_cellWidth - screenY / this.m_cellHeight;
    }
    
    public float screenToIsoX(final float screenX, final float screenY, final float isoAltitude) {
        return screenX / this.m_cellWidth - (screenY - isoAltitude * this.m_elevationUnit) / this.m_cellHeight;
    }
    
    public float screenToIsoY(final float screenX, final float screenY) {
        return -(screenX / this.m_cellWidth + screenY / this.m_cellHeight);
    }
    
    public float screenToIsoY(final float screenX, final float screenY, final float isoAltitude) {
        return -(screenX / this.m_cellWidth + (screenY - isoAltitude * this.m_elevationUnit) / this.m_cellHeight);
    }
    
    protected long computeWorldCellZ(final int x, final int y, final int z, final int zDelta) {
        return LayerOrder.computeZOrder(x, y, z, zDelta);
    }
    
    public ArrayList<Entity> getSortedEntities() {
        return this.m_sortedEntities;
    }
    
    public boolean initialize(final Maskable maskable, final Entity entity, final int x, final int y, final int z, final int deltaZ, final boolean canBeSetOnEmptyElement) {
        entity.m_zOrder = this.computeWorldCellZ(x, y, z, deltaZ);
        return true;
    }
    
    public final boolean initialize(final Maskable maskable, final Entity entity, final int x, final int y, final int z, final int deltaZ) {
        return this.initialize(maskable, entity, x, y, z, deltaZ, false);
    }
    
    public float[] getLayerColor(final Maskable maskable) {
        return GroupLayerManager.getInstance().getLayerColor(maskable);
    }
    
    public void addEntities(final ArrayList<DisplayedScreenElement> elements) {
        this.m_entityFilter.addEntities(this, elements);
    }
    
    public boolean accept(final AnimatedElement animatedElement) {
        return this.m_entityFilter.accept(animatedElement);
    }
    
    public static float convertScreenToIsoX(final int x, final int y) {
        return (x - y) * 86.0f * 0.5f;
    }
    
    public static float convertScreenToIsoY(final int x, final int y) {
        return -(x + y) * 43.0f * 0.5f;
    }
    
    static {
        IsoWorldScene.DEFAULT_MIN_ZOOM_FACTOR = 1.0f;
        IsoWorldScene.DEFAULT_DEFAULT_ZOOM_FACTOR = 1.2f;
        IsoWorldScene.DEFAULT_MAX_ZOOM_FACTOR = 2.2f;
    }
}
