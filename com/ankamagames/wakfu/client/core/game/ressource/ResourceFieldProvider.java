package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.weather.*;
import com.ankamagames.wakfu.client.core.game.weather.*;

public class ResourceFieldProvider extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String ICON_URL = "iconUrl";
    public static final String EXTENDED_MIN_TEMPERATURE = "extendedMinTemperature";
    public static final String EXTENDED_MAX_TEMPERATURE = "extendedMaxTemperature";
    public static final String IDEAL_MIN_TEMPERATURE = "idealMinTemperature";
    public static final String IDEAL_MAX_TEMPERATURE = "idealMaxTemperature";
    public static final String IDEAL_RAIN_ICON_URL = "idealRainIconUrl";
    private final ReferenceResource m_resource;
    private final int m_level;
    
    public ResourceFieldProvider(final ReferenceResource resource, final int level) {
        super();
        this.m_resource = resource;
        this.m_level = level;
    }
    
    public ReferenceResource getResource() {
        return this.m_resource;
    }
    
    public int getLevel() {
        return this.m_level;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_resource.getResourceName();
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getItemBigIconUrl(this.m_resource.getIconGfxId());
        }
        if (fieldName.equals("extendedMinTemperature")) {
            return this.m_resource.getExtendedTemperatureMin() + "°";
        }
        if (fieldName.equals("extendedMaxTemperature")) {
            return this.m_resource.getExtendedTemperatureMax() + "°";
        }
        if (fieldName.equals("idealMinTemperature")) {
            return this.m_resource.getIdealTemperatureMin() + "°";
        }
        if (fieldName.equals("idealMaxTemperature")) {
            return this.m_resource.getIdealTemperatureMax() + "°";
        }
        if (fieldName.equals("idealRainIconUrl")) {
            final float idealRainAverage = this.m_resource.getIdealRainMin() + (this.m_resource.getIdealRainMax() - this.m_resource.getIdealRainMin()) / 2.0f;
            final Humidity humidity = Humidity.getFromRainIntensity(idealRainAverage);
            return WeatherInfoFieldProvider.getHumidityIconURL(humidity);
        }
        return null;
    }
}
