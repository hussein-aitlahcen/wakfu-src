package com.ankamagames.wakfu.client.core.game.weather;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.weather.*;
import com.ankamagames.wakfu.client.core.*;

public class WeatherInfoFieldProvider implements FieldProvider
{
    public static final String ICON_URL = "iconUrl";
    public static final String DAY_DESCRIPTION = "dayDescription";
    public static final String ACTUAL_MIN_TEMPERATURE = "actualMinTemperature";
    public static final String ACTUAL_MAX_TEMPERATURE = "actualMaxTemperature";
    public static final String MIN_TEMPERATURE = "minTemperature";
    public static final String MAX_TEMPERATURE = "maxTemperature";
    public static final String MIN_TEMPERATURE_WITH_MODS = "minTemperatureWithMods";
    public static final String MAX_TEMPERATURE_WITH_MODS = "maxTemperatureWithMods";
    public static final String CURRENT_TEMPERATURE = "currentTemperature";
    public static final String SEASON_DESCRIPTION = "seasonDescription";
    public static final String WIND_FORCE_ICON_URL = "windForceIconUrl";
    public static final String WIND_STRENGTH = "windStrength";
    public static final String RAIN_STRENGTH = "rainStrength";
    public static final String WIND_STRENGTH_DESC = "windStrengthDescription";
    public static final String RAIN_STRENGTH_DESC = "rainStrengthDescription";
    private Season m_season;
    private WeatherInfo m_weatherInfo;
    private int m_dayInWeek;
    
    public WeatherInfoFieldProvider() {
        super();
        this.m_season = Season.SPRING;
        this.m_weatherInfo = new WeatherInfo();
    }
    
    public WeatherInfo getWeatherInfo() {
        return this.m_weatherInfo;
    }
    
    public void setSeason(final Season season) {
        this.m_season = season;
    }
    
    public Season getSeason() {
        return this.m_season;
    }
    
    public int getDayInWeek() {
        return this.m_dayInWeek;
    }
    
    public void setDayInWeek(final int dayInWeek) {
        this.m_dayInWeek = dayInWeek;
    }
    
    public void setWeatherInfo(final WeatherInfo weatherInfo) {
        this.m_weatherInfo = weatherInfo;
        this.m_season = ((weatherInfo == null) ? null : Season.fromDate(WakfuGameCalendar.getInstance().getDate()));
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentTemperature", "actualMinTemperature", "actualMaxTemperature", "minTemperature", "maxTemperature", "minTemperatureWithMods", "maxTemperatureWithMods", "iconUrl", "dayDescription", "windStrength", "windStrengthDescription", "rainStrength", "rainStrengthDescription", "windForceIconUrl", "seasonDescription");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    public static String getHumidityIconURL(final Humidity weather) {
        return WakfuConfiguration.getInstance().getIconUrl("weatherIconsPath", "defaultIconPath", weather.name().toLowerCase());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_weatherInfo == null) {
            return null;
        }
        if (fieldName.equals("iconUrl")) {
            return getHumidityIconURL(this.m_weatherInfo.getHumidityType());
        }
        if (fieldName.equals("dayDescription")) {
            return WakfuTranslator.getInstance().getString("day." + this.m_dayInWeek);
        }
        if (fieldName.equals("actualMinTemperature")) {
            final String temperature = String.format("%.0f", this.m_weatherInfo.getSeasonMinTemperature());
            return WakfuTranslator.getInstance().getString("weather.info.minTemperature", temperature);
        }
        if (fieldName.equals("actualMaxTemperature")) {
            final String temperature = String.format("%.0f", this.m_weatherInfo.getSeasonMaxTemperature());
            return WakfuTranslator.getInstance().getString("weather.info.maxTemperature", temperature);
        }
        if (fieldName.equals("minTemperature")) {
            final String temperature = String.format("%.0f", this.m_weatherInfo.getMinTemperature());
            return WakfuTranslator.getInstance().getString("weather.info.minTemperature", temperature);
        }
        if (fieldName.equals("maxTemperature")) {
            final String temperature = String.format("%.0f", this.m_weatherInfo.getMaxTemperature());
            return WakfuTranslator.getInstance().getString("weather.info.maxTemperature", temperature);
        }
        if (fieldName.equals("minTemperatureWithMods")) {
            final String realMinTemp = String.format("%.0f", Math.min(this.m_weatherInfo.getSeasonMaxTemperature(), Math.max(this.m_weatherInfo.getMinTemperature() + this.m_weatherInfo.getModTemperature(), this.m_weatherInfo.getSeasonMinTemperature())));
            return realMinTemp + "°";
        }
        if (fieldName.equals("maxTemperatureWithMods")) {
            final String realMaxTemp = String.format("%.0f", Math.max(this.m_weatherInfo.getSeasonMinTemperature(), Math.min(this.m_weatherInfo.getMaxTemperature() + this.m_weatherInfo.getModTemperature(), this.m_weatherInfo.getSeasonMaxTemperature())));
            return realMaxTemp + "°";
        }
        if (fieldName.equals("currentTemperature")) {
            final String temperature = String.format("%.0f", this.m_weatherInfo.getTemperature());
            return temperature + "°";
        }
        if (fieldName.equals("rainStrength")) {
            return this.m_weatherInfo.getRainEventIntensity();
        }
        if (fieldName.equals("windStrength")) {
            return this.m_weatherInfo.getWindForce();
        }
        if (fieldName.equals("rainStrengthDescription")) {
            final int value = (int)(this.m_weatherInfo.getRainEventIntensity() * 100.0f);
            return WakfuTranslator.getInstance().getString("weather.info.rainIntensityValue", value);
        }
        if (fieldName.equals("windStrengthDescription")) {
            final int value = (int)(this.m_weatherInfo.getWindForce() * 100.0f);
            return WakfuTranslator.getInstance().getString("weather.info.windForceValue", value);
        }
        if (fieldName.equals("seasonDescription")) {
            return WakfuTranslator.getInstance().getString("season." + this.m_season.name().toLowerCase());
        }
        if (fieldName.equals("windForceIconUrl")) {
            return this.m_weatherInfo.getWindSymbolicForce().getIconUrl();
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
}
