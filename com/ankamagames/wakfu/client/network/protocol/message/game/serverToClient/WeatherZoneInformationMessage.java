package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class WeatherZoneInformationMessage extends InputOnlyProxyMessage
{
    private int m_zoneId;
    private byte m_temperature;
    private int m_rainDuration;
    private float m_rainIntensity;
    private float m_wind;
    private float m_dayTMin;
    private float m_dayTMax;
    private float m_dayTMod;
    private float m_dayWindForce;
    private float m_dayWindMod;
    private float m_dayPrecipitations;
    private float m_dayPrecipitationsMod;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_zoneId = buffer.getInt();
        this.m_temperature = buffer.get();
        this.m_rainDuration = (buffer.getShort() & 0xFFFF);
        this.m_rainIntensity = buffer.getFloat();
        this.m_wind = buffer.getFloat();
        this.m_dayTMin = buffer.getFloat();
        this.m_dayTMax = buffer.getFloat();
        this.m_dayTMod = buffer.getFloat();
        this.m_dayWindForce = buffer.getFloat();
        this.m_dayWindMod = buffer.getFloat();
        this.m_dayPrecipitations = buffer.getFloat();
        this.m_dayPrecipitationsMod = buffer.getFloat();
        return true;
    }
    
    @Override
    public int getId() {
        return 9504;
    }
    
    public int getZoneId() {
        return this.m_zoneId;
    }
    
    public int getTemperature() {
        return this.m_temperature;
    }
    
    public int getRainDuration() {
        return this.m_rainDuration;
    }
    
    public float getRainIntensity() {
        return this.m_rainIntensity;
    }
    
    public float getWind() {
        return this.m_wind;
    }
    
    public float getDayTMin() {
        return this.m_dayTMin;
    }
    
    public float getDayTMax() {
        return this.m_dayTMax;
    }
    
    public float getDayTMod() {
        return this.m_dayTMod;
    }
    
    public float getDayWindForce() {
        return this.m_dayWindForce;
    }
    
    public float getDayWindMod() {
        return this.m_dayWindMod;
    }
    
    public float getDayPrecipitations() {
        return this.m_dayPrecipitations;
    }
    
    public float getDayPrecipitationsMod() {
        return this.m_dayPrecipitationsMod;
    }
}
