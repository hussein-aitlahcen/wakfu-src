package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.sound.group.*;

public class WeatherSoundEvent extends SoundEvent
{
    public static final byte WEATHER_SOUND_EVENT_TYPE_ID = 0;
    private WeatherEventType m_weatherType;
    
    public WeatherSoundEvent(final WeatherEventType weather) {
        this(weather, null);
    }
    
    public WeatherSoundEvent(final WeatherEventType weather, final ObservedSource source) {
        super(EventType.WEATHER_EVENT, source);
        this.m_weatherType = weather;
    }
    
    public WeatherEventType getWeatherType() {
        return this.m_weatherType;
    }
    
    @Override
    public byte getSoundEventType() {
        return 0;
    }
    
    @Override
    public int getSignature() {
        return this.m_weatherType.getSubType() << 8 | 0x0;
    }
    
    @Override
    public String getEventTitle() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Weather - ").append(this.m_weatherType.getDescription());
        return sb.toString();
    }
    
    @Override
    public String getParamDescription() {
        final StringBuilder sb = new StringBuilder();
        final ObservedSource source = this.getSource();
        if (source != null) {
            sb.append(" Position = [").append(source.getObservedX()).append(",").append(source.getObservedY()).append("]");
        }
        return sb.toString();
    }
}
