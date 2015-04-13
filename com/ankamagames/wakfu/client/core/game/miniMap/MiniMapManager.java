package com.ankamagames.wakfu.client.core.game.miniMap;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.reflect.*;

public class MiniMapManager extends AbstractMapManager
{
    private static final Logger m_logger;
    private static final MiniMapManager m_instance;
    private static final String BACKGROUND_COLOR = "bgColor";
    private Color m_bgColor;
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("bgColor")) {
            return this.m_bgColor;
        }
        return super.getFieldValue(fieldName);
    }
    
    public static MiniMapManager getInstance() {
        return MiniMapManager.m_instance;
    }
    
    @Override
    public void initProperty() {
        super.initProperty();
        PropertiesProvider.getInstance().setPropertyValue("miniMap", this);
        PropertiesProvider.getInstance().setPropertyValue("ambienceZone", WakfuAmbianceListener.getInstance());
    }
    
    @Override
    public float getZoomScale() {
        return WakfuClientInstance.getInstance().getGamePreferences().getFloatValue(WakfuKeyPreferenceStoreEnum.RADAR_ZOOM_SCALE_PREFERENCE_KEY);
    }
    
    @Override
    public void setZoomScale(final float zoomScale) {
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.RADAR_ZOOM_SCALE_PREFERENCE_KEY, zoomScale);
        super.setZoomScale(zoomScale);
    }
    
    @Override
    public DisplayableMapPointIcon getDefaultMapPoint() {
        return DisplayableMapPointIconFactory.MINIMAP_POINT_BIG_ICON;
    }
    
    @Override
    public void setMap(final WakfuParentMapZoneDescription zoneDescription) {
        if (this.isHavenWorld()) {
            this.m_bgColor = new Color(152, 205, 221, 255);
            this.setHavenWorldMap();
        }
        else {
            super.setMap(zoneDescription);
            final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(this.getMap());
            if (worldInfo != null) {
                this.m_bgColor = worldInfo.m_papermapBgColor;
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isMapAvailable", "bgColor");
    }
    
    @Override
    protected void updateMapZoneToWidget() {
    }
    
    @Override
    protected void updateTerritoryVisibility() {
    }
    
    public void refreshHavenWorld() {
        if (this.isHavenWorld()) {
            this.setHavenWorldMap();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MiniMapManager.class);
        m_instance = new MiniMapManager();
    }
}
