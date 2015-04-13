package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public abstract class AbstractMapManager extends ImmutableFieldProvider implements TargetPositionListener<PathMobile>
{
    private static final Logger m_logger;
    public static final String MAP = "map";
    public static final String MAP_ID = "mapId";
    public static final String ZOOM_SCALE = "zoomScale";
    public static final String CENTER_X = "centerX";
    public static final String CENTER_Y = "centerY";
    public static final String CENTER_Z = "centerZ";
    public static final String IS_MAP_AVAILABLE_FIELD = "isMapAvailable";
    public static final String LANDMARKS = "landmarks";
    public static final String[] FIELDS;
    private static final String[] CENTER_FILES;
    private double m_centerX;
    private double m_centerY;
    private double m_centerZ;
    private long m_centerTargetId;
    protected MapOverlay m_widget;
    private final LandMarkHandler m_landMarkHandler;
    protected WakfuParentMapZoneDescription m_zoneDescription;
    
    public AbstractMapManager() {
        super();
        this.m_landMarkHandler = new LandMarkHandler();
    }
    
    public void initProperty() {
        PropertiesProvider.getInstance().setPropertyValue("landMark.filters", this.m_landMarkHandler.getFilters());
    }
    
    public MapOverlay getWidget() {
        return this.m_widget;
    }
    
    public void setWidget(final MapOverlay widget) {
        this.m_widget = widget;
        this.m_landMarkHandler.setWidget(this.m_widget);
        if (this.m_widget != null && this.isHavenWorld()) {
            this.setHavenWorldMap();
        }
    }
    
    public ParentMapZoneDescription getZoneDescription() {
        return this.m_zoneDescription;
    }
    
    public void setCenterTargetId(final long centerTargetId) {
        this.m_centerTargetId = centerTargetId;
    }
    
    public void setMap(final WakfuParentMapZoneDescription zoneDescription) {
        if (this.getWidget() != null) {
            this.getWidget().setForceDisplayEntity(false);
        }
        this.m_zoneDescription = zoneDescription;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "map", "mapId");
        if (this.m_zoneDescription != null) {
            this.m_zoneDescription.onLoad(this);
        }
        this.onMapChanged();
    }
    
    @Override
    public void cellPositionChanged(final PathMobile mobile, final int x, final int y, final short altitude) {
        this.m_landMarkHandler.updatePoint(mobile.getId(), 0, x, y, altitude, WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId());
    }
    
    public void updateCenterFromCamera() {
        final AleaIsoCamera cam = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        this.setCenter(cam.getCameraExactIsoWorldX(), cam.getCameraExactIsoWorldY(), cam.getAltitude());
    }
    
    public abstract float getZoomScale();
    
    public void setZoomScale(final float zoomScale) {
        this.fireZoomFieldChanged();
    }
    
    public double getCenterX() {
        return this.m_centerX;
    }
    
    public double getCenterY() {
        return this.m_centerY;
    }
    
    public double getCenterZ() {
        return this.m_centerZ;
    }
    
    public void setCenter(final double centerX, final double centerY, final double centerZ) {
        this.m_centerX = centerX;
        this.m_centerY = centerY;
        this.m_centerZ = centerZ;
        this.fireCenterFieldChanged();
    }
    
    private void fireZoomFieldChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "zoomScale");
    }
    
    private void fireCenterFieldChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, AbstractMapManager.CENTER_FILES);
    }
    
    @Nullable
    protected String getMapPath() {
        return (this.m_zoneDescription != null) ? this.m_zoneDescription.getMapUrl() : null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isMapAvailable")) {
            return this.isMapAvailable();
        }
        if (fieldName.equals("map")) {
            return this.getMapPath();
        }
        if (fieldName.equals("mapId")) {
            return this.getMap();
        }
        if (fieldName.equals("zoomScale")) {
            return this.getZoomScale();
        }
        if (fieldName.equals("centerX")) {
            return this.getCenterX();
        }
        if (fieldName.equals("centerY")) {
            return this.getCenterY();
        }
        if (fieldName.equals("centerZ")) {
            return this.getCenterZ();
        }
        if (fieldName.equals("landmarks")) {
            return this.m_landMarkHandler;
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return AbstractMapManager.FIELDS;
    }
    
    public abstract DisplayableMapPointIcon getDefaultMapPoint();
    
    public boolean canZoomIn() {
        return this.m_zoneDescription != null && this.m_zoneDescription.canZoomIn();
    }
    
    public boolean canZoomOut() {
        return this.m_zoneDescription != null && this.m_zoneDescription.canZoomOut();
    }
    
    public void zoomIn() {
        if (this.m_zoneDescription != null && this.m_zoneDescription.canZoomIn()) {
            this.m_zoneDescription.zoomIn(this);
        }
    }
    
    public void zoomOut() {
        if (this.m_zoneDescription != null && this.m_zoneDescription.canZoomOut()) {
            this.m_zoneDescription.zoomOut(this);
        }
    }
    
    private void doThingsOnMapChange() {
        this.onMapChanged();
    }
    
    protected void onMapChanged() {
        final MapOverlay widget = this.getWidget();
        final short map = this.getMap();
        final boolean display = map == WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId();
        if (widget != null) {
            widget.setDisplayPoints(true);
            widget.setDisplayCompass(true);
        }
        this.updateMapZoneToWidget();
        this.updateTerritoryVisibility();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "map");
    }
    
    public boolean isMapAvailable() {
        if (this.isHavenWorld()) {
            return true;
        }
        final String path = this.getMapPath();
        return path != null && URLUtils.urlExists(path);
    }
    
    public final boolean isHavenWorld() {
        return HavenWorldManager.INSTANCE.hasHavenWorld();
    }
    
    protected final void setHavenWorldMap() {
        if (this.getWidget() == null) {
            return;
        }
        final HavenWorldTopology havenWorld = HavenWorldManager.INSTANCE.getHavenWorld();
        HavenWorldEntityCreator.create(havenWorld, this.getWidget());
        this.m_landMarkHandler.setHavenWorld();
    }
    
    public short getMap() {
        if (this.isHavenWorld()) {
            return HavenWorldManager.INSTANCE.getHavenWorld().getWorldId();
        }
        if (this.m_zoneDescription instanceof InstanceParentMapZoneDescription) {
            return (short)this.m_zoneDescription.getId();
        }
        if (this.m_zoneDescription instanceof SubInstanceParentMapZoneDescription) {
            return ((SubInstanceParentMapZoneDescription)this.m_zoneDescription).getInstanceId();
        }
        return -1;
    }
    
    public boolean isDisplayingCurrentMap() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId() == this.getMap();
    }
    
    protected abstract void updateMapZoneToWidget();
    
    protected abstract void updateTerritoryVisibility();
    
    public LandMarkHandler getLandMarkHandler() {
        return this.m_landMarkHandler;
    }
    
    public void addPoint(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final String particlePath, final float[] color) {
        this.m_landMarkHandler.addPoint(referenceId, type, worldX, worldY, altitude, name, value, icon, particlePath, color, instanceId);
    }
    
    public void removePoint(final int type, final long referenceId) {
        this.m_landMarkHandler.removePoint(type, referenceId);
    }
    
    public void addCompassPoint(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color) {
        this.m_landMarkHandler.addCompassPoint(referenceId, type, worldX, worldY, altitude, instanceId, name, value, icon, color);
    }
    
    public void removeCompass(final int type, final long referenceId) {
        this.m_landMarkHandler.removeCompass(type, referenceId);
    }
    
    public void setUniqueCompassPoint(final float worldX, final float worldY, final float altitude, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color) {
        this.m_landMarkHandler.setUniqueCompassPoint(worldX, worldY, altitude, instanceId, name, value, icon, color);
    }
    
    protected DisplayableMapPoint getUniqueCompassPoint() {
        return this.m_landMarkHandler.getUniqueCompassPoint();
    }
    
    public void removeUniqueCompass() {
        this.m_landMarkHandler.removeUniqueCompass();
    }
    
    public void removeAll() {
        this.m_landMarkHandler.removeAll();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMapManager.class);
        FIELDS = new String[] { "map", "zoomScale", "centerX", "centerY", "centerZ", "isMapAvailable" };
        CENTER_FILES = new String[] { "centerX", "centerY", "centerZ" };
    }
}
