package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public final class WeatherEffectsVisibilityManager implements ReachTargetListener, TargetPositionListener<PathMobile>
{
    private static final Logger m_logger;
    public static final WeatherEffectsVisibilityManager INSTANCE;
    private boolean m_weatherEffectsVisible;
    
    public WeatherEffectsVisibilityManager() {
        super();
        this.m_weatherEffectsVisible = true;
    }
    
    @Override
    public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
        final DisplayedScreenElement walkableElement = AleaIsoCamera.getDisplayedScreenElement(DisplayedScreenWorld.getInstance(), worldX, worldY, altitude);
        if (walkableElement == null) {
            return;
        }
        final boolean visible = GroupLayerManager.getInstance().isOutdoorVisibleFrom(walkableElement.getMaskKey());
        this.changeVisibility(visible);
    }
    
    @Override
    public void onTargetReached() {
        final boolean visible = GroupLayerManager.getInstance().outdoorVisibleFromCamera();
        this.changeVisibility(visible);
    }
    
    private void changeVisibility(final boolean visible) {
        if (visible != this.m_weatherEffectsVisible) {
            WeatherEffectManager.INSTANCE.setEffectsVisibility(visible);
            this.m_weatherEffectsVisible = visible;
        }
    }
    
    public boolean isWeatherEffectsVisible() {
        return this.m_weatherEffectsVisible;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WeatherEffectsVisibilityManager.class);
        INSTANCE = new WeatherEffectsVisibilityManager();
    }
}
