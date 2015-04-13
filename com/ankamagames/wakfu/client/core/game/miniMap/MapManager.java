package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.baseImpl.graphics.game.worldPositionManager.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.component.map.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.landMarks.agtEnum.*;

public class MapManager extends AbstractMapManager implements ValuePointDeleteListener
{
    private static final Logger m_logger;
    private static final MapManager m_instance;
    public static final String CURRENT_MAP_NAME_FIELD = "currentMapName";
    public static final String CURRENT_TERRITORY_NAME_FIELD = "currentTerritoryName";
    public static final String CURRENT_TERRITORY_ICON_URL_FIELD = "currentTerritoryIconUrl";
    public static final String CURRENT_PROTECTOR_DESCRIPTION = "currentProtectorDescription";
    public static final String AVAILABLE_MAPS_COORDS_FIELD = "availableMapsCoords";
    public static final String DISPLAYED_POSITION_FIELD = "displayedPosition";
    public static final String DISPLAY_TERRITORIES_FIELD = "displayTerritories";
    public static final String RECOMMENDED_LEVEL = "recommendedLevel";
    public static final String PROTECTOR_ANIMATED_ELEM = "protectorAnimatedElem";
    public static final String PROTECTOR_ANIM_NAME = "protectorAnimName";
    public static final String[] FIELDS;
    public static final String[] ALL_FIELDS;
    private static final int FADE_TIME = 1000;
    private int m_protectorId;
    private AnimatedElement m_protectorAnim;
    private float m_zoomScale;
    private int m_displayedX;
    private int m_displayedY;
    private boolean m_displayTerritories;
    private AudioSource m_ambiance;
    
    private MapManager() {
        super();
        this.m_protectorId = -1;
        this.m_zoomScale = 1.0f;
        MapManagerHelper.loadMapDefinition();
    }
    
    public String getMapName() {
        final short map = this.getMap();
        if (map != -1) {
            return WakfuTranslator.getInstance().getString(77, map, new Object[0]);
        }
        return null;
    }
    
    public static MapManager getInstance() {
        return MapManager.m_instance;
    }
    
    public void setDisplayTerritories(final boolean displayTerritories) {
        if (this.m_displayTerritories == displayTerritories) {
            return;
        }
        this.m_displayTerritories = displayTerritories;
        this.updateTerritoryVisibility();
    }
    
    @Override
    public void initProperty() {
        super.initProperty();
        PropertiesProvider.getInstance().setPropertyValue("map", this);
    }
    
    @Override
    public MapWidget getWidget() {
        return (MapWidget)this.m_widget;
    }
    
    public void setWidget(final MapWidget widget) {
        super.setWidget(widget);
        this.updateMapZoneToWidget();
        if (widget == null) {
            this.stopAmbianceSound();
        }
    }
    
    public void addCompassPointAndPositionMarker(final float worldX, final float worldY, final float worldZ, final short instanceId, final Object value, final boolean forceZ) {
        this.addCompassPointAndPositionMarker(2, worldX, worldY, worldZ, instanceId, value, null, forceZ);
    }
    
    public void removeCompassPointAndPositionMarker() {
        this.removeUniqueCompass();
        WorldPositionMarkerManager.getInstance().removeAll();
    }
    
    public void addCompassPointAndPositionMarker(final int type, final float worldX, final float worldY, final float worldZ, final short instanceId, final Object value, String name, final boolean forceZ) {
        if (name == null) {
            name = WakfuTranslator.getInstance().getString("compass");
        }
        this.addCompassPointAndPositionMarker(0L, type, worldX, worldY, worldZ, instanceId, name, value, DisplayableMapPointIconFactory.COMPASS_POINT_ICON, WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, forceZ);
    }
    
    public void addCompassPointAndPositionMarker(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color, final boolean forceZ) {
        WorldPositionMarkerManager.getInstance().setPoint(type, referenceId, (int)worldX, (int)worldY, (int)altitude, value, this, forceZ);
        this.setUniqueCompassPoint(worldX, worldY, altitude, instanceId, name, value, icon, color);
        this.getUniqueCompassPoint().setDndropable(true);
    }
    
    public void removeCompassPointAndPositionMarker(final int type, final long referenceId) {
        this.removeUniqueCompass();
        WorldPositionMarkerManager.getInstance().removePoint(type, referenceId);
    }
    
    public boolean hasCompassPointOfType(final int type) {
        return WorldPositionMarkerManager.getInstance().hasPoint(type);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("availableMapsCoords")) {
            return PaperMapManager.getInstance().getAvailableMapCoordsHash().toNativeArray();
        }
        if (fieldName.equals("displayedPosition")) {
            return new StringBuilder("x=").append(this.m_displayedX).append(" y=").append(this.m_displayedY);
        }
        if (fieldName.equals("displayTerritories")) {
            return this.m_displayTerritories;
        }
        if (fieldName.equals("currentTerritoryName")) {
            if (this.isHavenWorld()) {
                return WakfuTranslator.getInstance().getString(77, HavenWorldManager.INSTANCE.getHavenWorld().getWorldId(), new Object[0]);
            }
            final int territoryId = this.getDisplayedTerritoryId();
            if (territoryId != -1) {
                return WakfuTranslator.getInstance().getString(66, territoryId, new Object[0]);
            }
            return null;
        }
        else if (fieldName.equals("currentMapName")) {
            if (this.isHavenWorld()) {
                return WakfuTranslator.getInstance().getString("desc.havenWorld");
            }
            return this.getMapName();
        }
        else if (fieldName.equals("currentTerritoryIconUrl")) {
            final ProtectorBase protector = this.getCurrentProtector();
            if (protector == null) {
                return null;
            }
            return WakfuConfiguration.getInstance().getIconUrl("nationFlagIconsPath", null, protector.getCurrentNationId());
        }
        else {
            if (fieldName.equals("protectorAnimatedElem")) {
                return this.m_protectorAnim;
            }
            if (fieldName.equals("protectorAnimName")) {
                return ProtectorMood.NEUTRAL.getAnimation();
            }
            if (fieldName.equals("currentProtectorDescription")) {
                final ProtectorBase protector = this.getCurrentProtector();
                if (protector == null) {
                    return null;
                }
                final TextWidgetFormater sb = new TextWidgetFormater();
                ProtectorDescriptionHelper.describeProtectorAndTerritory(sb, (Protector)protector);
                return sb.finishAndToString();
            }
            else {
                if (!fieldName.equals("recommendedLevel")) {
                    return super.getFieldValue(fieldName);
                }
                final Territory territory = (Territory)TerritoryManager.INSTANCE.getTerritory(this.getDisplayedTerritoryId());
                if (territory == null) {
                    return null;
                }
                final short minLevel = territory.getMinLevel();
                final short maxLevel = territory.getMaxLevel();
                if (minLevel != -1 && maxLevel != -1) {
                    return StringFormatter.format(WakfuTranslator.getInstance().getString("recommended.level", minLevel, maxLevel), new Object[0]);
                }
                return null;
            }
        }
    }
    
    private ProtectorBase getCurrentProtector() {
        final Territory territory = (Territory)TerritoryManager.INSTANCE.getTerritory(this.getDisplayedTerritoryId());
        if (territory == null) {
            return null;
        }
        final ProtectorBase protector = territory.getProtector();
        if (protector == null) {
            return null;
        }
        return protector;
    }
    
    public int getDisplayedTerritoryId() {
        if (this.m_zoneDescription instanceof SubInstanceParentMapZoneDescription) {
            return this.m_zoneDescription.getId();
        }
        return -1;
    }
    
    @Override
    public String[] getFields() {
        return MapManager.ALL_FIELDS;
    }
    
    public void setDisplayedPosition(final int displayedX, final int displayedY) {
        this.m_displayedX = displayedX;
        this.m_displayedY = displayedY;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "displayedPosition");
    }
    
    @Override
    public float getZoomScale() {
        return this.m_zoomScale;
    }
    
    @Override
    public void setZoomScale(final float zoomScale) {
        super.setZoomScale(this.m_zoomScale = zoomScale);
    }
    
    @Override
    protected void onMapChanged() {
        super.onMapChanged();
        this.setCurrentProtectorAnimation(this.getCurrentProtector());
        this.updateSoundAmbiance();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentTerritoryName", "currentProtectorDescription", "currentTerritoryName", "currentMapName", "currentTerritoryIconUrl", "recommendedLevel");
    }
    
    private void updateSoundAmbiance() {
        if (!WakfuGameEntity.getInstance().hasFrame(UIMapFrame.getInstance())) {
            return;
        }
        long soundId;
        if (this.m_zoneDescription instanceof CompleteMapParentMapZoneDescription) {
            final CompleteMapParentMapZoneDescription zoneDescription = (CompleteMapParentMapZoneDescription)this.m_zoneDescription;
            soundId = zoneDescription.getSoundId();
        }
        else {
            soundId = 600197L;
        }
        if (this.m_ambiance == null || this.m_ambiance.getFileIdValue() != soundId) {
            this.stopAmbianceSound();
            if (soundId != -1L) {
                this.m_ambiance = WakfuSoundManager.getInstance().playGUISound(soundId, true);
                if (this.m_ambiance != null) {
                    this.m_ambiance.setGain(0.0f);
                    this.m_ambiance.fade(1.0f, 1000.0f);
                }
            }
        }
    }
    
    private void stopAmbianceSound() {
        if (this.m_ambiance != null) {
            this.m_ambiance.setStopOnNullGain(true);
            this.m_ambiance.fade(0.0f, 1000.0f);
            this.m_ambiance = null;
        }
    }
    
    @Override
    protected void updateTerritoryVisibility() {
        final MapWidget widget = this.getWidget();
        if (widget == null) {
            return;
        }
        widget.setAllMapZonesVisible(this.m_displayTerritories);
        final boolean canZoomIn = this.canZoomIn();
        if (canZoomIn) {
            widget.setDragEnabled(false);
            widget.setDropEnabled(false);
            widget.setLandMarkZoom(0.75f);
            widget.setUseAlternateTexture(true);
        }
        else {
            widget.setDragEnabled(true);
            widget.setDropEnabled(true);
            widget.setLandMarkZoom(1.0f);
            widget.setUseAlternateTexture(false);
        }
    }
    
    public void setSecondaryMapToSelectedMapZone() {
        final MapZone mapZone = this.getWidget().getSelectedMapZone();
        if (mapZone == null) {
            return;
        }
        final WakfuMapZoneDescription description = (WakfuMapZoneDescription)mapZone.getMapZoneDescription();
        if (!description.canZoomIn()) {
            return;
        }
        description.sendZoneToMap(this);
    }
    
    @Override
    public boolean isMapAvailable() {
        return !WakfuGameEntity.getInstance().hasFrame(UIDragoMapFrame.getInstance()) && super.isMapAvailable();
    }
    
    @Override
    public void setMap(final WakfuParentMapZoneDescription zoneDescription) {
        if (this.isHavenWorld()) {
            this.setHavenWorldMap();
        }
        else {
            super.setMap(zoneDescription);
        }
        if (!this.isMapAvailable() && WakfuGameEntity.getInstance().hasFrame(UIMapFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIMapFrame.getInstance());
        }
        this.setCurrentProtectorAnimation(this.getCurrentProtector());
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isMapAvailable", "currentMapName", "currentProtectorDescription", "currentTerritoryIconUrl", "currentTerritoryName", "recommendedLevel");
    }
    
    @Override
    public DisplayableMapPointIcon getDefaultMapPoint() {
        return DisplayableMapPointIconFactory.MINIMAP_POINT_BIG_ICON;
    }
    
    public void updateMapZoneToWidget() {
        final MapWidget widget = this.getWidget();
        if (widget == null) {
            return;
        }
        try {
            widget.setMapAnmPath(WakfuConfiguration.getContentPath("worldMapAnmFilePath"));
        }
        catch (PropertyException e) {
            MapManager.m_logger.error((Object)e.getMessage());
        }
        widget.removeAllMapZones();
        if (this.m_zoneDescription != null) {
            final Collection<? extends ParentMapZoneDescription> displayedZones = this.m_zoneDescription.getDisplayedZones();
            for (final ParentMapZoneDescription mapZone : displayedZones) {
                widget.addMapZone(mapZone);
            }
        }
        final Point3 pos = WakfuGameEntity.getInstance().getLocalPlayer().getPosition();
        widget.setPlayerMapZone(pos.getX(), pos.getY());
        this.updateTerritoryVisibility();
    }
    
    public void fireMapIsAvaiable() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isMapAvailable");
    }
    
    @Override
    public void onValuePointDelete() {
        this.removeCompassPointAndPositionMarker();
    }
    
    private void setCurrentProtectorAnimation(final ProtectorBase protector) {
        final int protectorId = (protector == null) ? -1 : protector.getId();
        if (this.m_protectorId == protectorId) {
            return;
        }
        this.m_protectorId = protectorId;
        if (this.m_protectorAnim != null) {
            this.m_protectorAnim.dispose();
            this.m_protectorAnim = null;
        }
        if (protectorId != -1) {
            this.m_protectorAnim = ProtectorDisplayHelper.createProtectorAnimation(protectorId);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "protectorAnimatedElem", "protectorAnimName");
    }
    
    public void onProtectorsUpdate() {
        this.getLandMarkHandler().updateLandMarkType(LandMarkEnum.PROTECTORS);
        if (this.m_zoneDescription instanceof SubInstanceParentMapZoneDescription || this.m_zoneDescription instanceof InstanceParentMapZoneDescription) {
            this.updateMapZoneToWidget();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapManager.class);
        m_instance = new MapManager();
        FIELDS = new String[] { "displayedPosition", "availableMapsCoords", "displayTerritories" };
        ALL_FIELDS = new String[MapManager.FIELDS.length + AbstractMapManager.FIELDS.length];
        System.arraycopy(MapManager.FIELDS, 0, MapManager.ALL_FIELDS, 0, MapManager.FIELDS.length);
        System.arraycopy(AbstractMapManager.FIELDS, 0, MapManager.ALL_FIELDS, MapManager.FIELDS.length, AbstractMapManager.FIELDS.length);
    }
}
