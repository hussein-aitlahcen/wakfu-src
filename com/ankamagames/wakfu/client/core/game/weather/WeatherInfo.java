package com.ankamagames.wakfu.client.core.game.weather;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.weather.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.common.game.protector.event.*;

public final class WeatherInfo
{
    protected static final Logger m_logger;
    private float m_minTemperature;
    private float m_maxTemperature;
    private float m_temperature;
    private float[] m_seasonMinMaxTemperatures;
    private float m_modTemperature;
    private Weather m_weatherType;
    private Humidity m_humidityType;
    private float m_rainEventIntensity;
    private int m_rainEventDuration;
    private float m_modRain;
    private float m_windForce;
    private float m_modWind;
    private WindForce m_windSymbolicForce;
    private boolean m_weatherChanged;
    private boolean m_dayWeatherChanged;
    private Weather m_dayWeatherType;
    private WindForce m_dayWindSymbolicForce;
    private float m_dayMinTemperature;
    private float m_dayMaxTemperature;
    private float m_precipitations;
    
    public WeatherInfo() {
        super();
        this.m_weatherType = Weather.SUNNY;
        this.m_humidityType = Humidity.DRY;
        this.m_rainEventIntensity = 0.0f;
        this.m_rainEventDuration = -1;
        this.m_windSymbolicForce = WindForce.NONE;
        this.m_weatherChanged = true;
        this.m_dayWeatherChanged = true;
        this.m_dayWeatherType = Weather.SUNNY;
        this.m_dayWindSymbolicForce = WindForce.NONE;
    }
    
    public float getPrecipitations() {
        return this.m_precipitations;
    }
    
    public float getTemperature() {
        return this.m_temperature;
    }
    
    public void setTemperature(final float temperature) {
        this.m_temperature = temperature;
    }
    
    public float getMinTemperature() {
        return this.m_minTemperature;
    }
    
    public float getMaxTemperature() {
        return this.m_maxTemperature;
    }
    
    public float getSeasonMinTemperature() {
        return (this.m_seasonMinMaxTemperatures != null) ? this.m_seasonMinMaxTemperatures[0] : 0.0f;
    }
    
    public float getSeasonMaxTemperature() {
        return (this.m_seasonMinMaxTemperatures != null) ? this.m_seasonMinMaxTemperatures[1] : 0.0f;
    }
    
    public float getModTemperature() {
        return this.m_modTemperature;
    }
    
    public float getRainEventIntensity() {
        return this.m_rainEventIntensity;
    }
    
    public float getWindForce() {
        return this.m_windForce;
    }
    
    public Weather getWeatherType() {
        return this.m_weatherType;
    }
    
    public Humidity getHumidityType() {
        return this.m_humidityType;
    }
    
    public WindForce getWindSymbolicForce() {
        return this.m_windSymbolicForce;
    }
    
    public Weather getDayWeatherType() {
        return this.m_dayWeatherType;
    }
    
    public WindForce getDayWindSymbolicForce() {
        return this.m_dayWindSymbolicForce;
    }
    
    public static WindForce windforceFromParam(final float windForce) {
        if (windForce < 0.33f) {
            return WindForce.NONE;
        }
        if (windForce < 0.66f) {
            return WindForce.MODERATE;
        }
        return WindForce.STRONG;
    }
    
    public void setWeather(final float temperature, final float minTemperature, final float maxTemperature, final float modTemperature, final float[] seasonMinMaxTemperatures, final float rainIntensity, final int rainDuration, final float modRain, final float wind, final float modWind) {
        final Weather weather = WeatherUtil.fromParams(rainIntensity + modRain, temperature + modTemperature, wind + modWind);
        if (weather != this.m_weatherType) {
            this.m_weatherChanged = true;
            this.m_weatherType = weather;
        }
        this.m_humidityType = Humidity.getFromRainIntensity(rainIntensity + modRain);
        this.m_rainEventIntensity = rainIntensity;
        this.m_rainEventDuration = rainDuration;
        this.m_temperature = temperature;
        this.m_minTemperature = minTemperature;
        this.m_maxTemperature = maxTemperature;
        this.m_seasonMinMaxTemperatures = seasonMinMaxTemperatures;
        this.m_modTemperature = modTemperature;
        this.m_modWind = modWind;
        this.m_modRain = modRain;
        this.m_windForce = wind;
        final WindForce windSymbolicForce = windforceFromParam(wind);
        if (this.m_windSymbolicForce != windSymbolicForce) {
            this.m_windSymbolicForce = windSymbolicForce;
            this.m_weatherChanged = true;
        }
    }
    
    public void setDayWeather(final float tMin, final float tMax, final float tMod, final float avgWind, final float windMod, final float avgPrecipitations, final float precipitationsMod) {
        this.m_dayMinTemperature = tMin + tMod;
        this.m_dayMaxTemperature = tMax + tMod;
        this.m_precipitations = avgPrecipitations;
        final float windStrength = avgWind + windMod;
        final Weather weather = WeatherUtil.fromParams(avgPrecipitations + precipitationsMod, (this.m_dayMinTemperature + this.m_dayMaxTemperature) * 0.5f, windStrength);
        if (weather != this.m_dayWeatherType) {
            this.m_dayWeatherChanged = true;
            this.m_dayWeatherType = weather;
        }
        final WindForce windForce = windforceFromParam(windStrength);
        if (windForce != this.m_dayWindSymbolicForce) {
            this.m_dayWeatherChanged = true;
            this.m_dayWindSymbolicForce = windForce;
        }
    }
    
    public void updateViews(final Protector protector) {
        WeatherInfoManager.getInstance().updateCurrentWeather(this);
        WeatherEffectManager.INSTANCE.changeTo(this.m_weatherType);
        WeatherEffectManager.INSTANCE.changeWindStrength(this.m_windForce);
        if (this.m_dayWeatherChanged) {
            this.m_dayWeatherChanged = false;
            final ProtectorDayWeatherUpdateEvent dayWeatherEvent = (ProtectorDayWeatherUpdateEvent)ProtectorEvents.DAY_WEATHER_UPDATE.create();
            dayWeatherEvent.setProtector(protector);
            ProtectorEventDispatcher.INSTANCE.dispatch(dayWeatherEvent);
        }
        if (this.m_weatherChanged) {
            this.m_weatherChanged = false;
        }
        WeatherInfo.m_logger.debug((Object)("****** Mise \u00e0 jour des vues relatives \u00e0 la m\u00e9t\u00e9o : " + this.toString()));
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("Weather {\n");
        buffer.append("\t").append("temperature : ").append(this.m_temperature).append("°").append("\n");
        buffer.append("\t").append("tendance : ").append(this.m_weatherType.toString()).append("\n");
        buffer.append("\t").append("precipitations : d=").append(this.m_rainEventDuration).append(", i=").append(this.m_rainEventIntensity).append("\n");
        buffer.append("\t").append("wind : ").append(this.m_windForce).append(" / 1.0\n");
        buffer.append("}\n");
        return buffer.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WeatherInfo.class);
    }
}
