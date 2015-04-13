package com.ankamagames.wakfu.client.core.game.weather;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import java.nio.*;

public class WeatherHistory
{
    private int m_historyLength;
    private Season[] m_seasons;
    private int[] m_minTemperatures;
    private int[] m_maxTemperatures;
    private int[] m_modTemperatures;
    private float[] m_avgWind;
    private float[] m_modWind;
    private float[] m_avgPrecipitations;
    private float[] m_modPrecipitations;
    private HashMap<Season, float[]> m_seasonTemperatureLimits;
    
    public int getHistoryLength() {
        return this.m_historyLength;
    }
    
    public Season getSeasons(final int index) {
        return this.m_seasons[index];
    }
    
    public int getMinTemperatures(final int index) {
        return this.m_minTemperatures[index];
    }
    
    public int getMaxTemperatures(final int index) {
        return this.m_maxTemperatures[index];
    }
    
    public int getModTemperatures(final int index) {
        return this.m_modTemperatures[index];
    }
    
    public float getAvgWind(final int index) {
        return this.m_avgWind[index];
    }
    
    public float getModWind(final int index) {
        return this.m_modWind[index];
    }
    
    public float getAvgPrecipitations(final int index) {
        return this.m_avgPrecipitations[index];
    }
    
    public float getModPrecipitations(final int index) {
        return this.m_modPrecipitations[index];
    }
    
    public float[] getSeasonMinMaxTemperatures(final Season season) {
        return this.m_seasonTemperatureLimits.get(season);
    }
    
    public void fromBuild(final ByteBuffer buffer) {
        this.m_historyLength = (buffer.get() & 0xFF);
        if (this.m_historyLength > 0) {
            this.m_seasons = new Season[this.m_historyLength];
            this.m_minTemperatures = new int[this.m_historyLength];
            this.m_maxTemperatures = new int[this.m_historyLength];
            this.m_avgWind = new float[this.m_historyLength];
            this.m_avgPrecipitations = new float[this.m_historyLength];
            this.m_modTemperatures = new int[this.m_historyLength];
            this.m_modWind = new float[this.m_historyLength];
            this.m_modPrecipitations = new float[this.m_historyLength];
            for (int i = 0; i < this.m_historyLength; ++i) {
                this.m_seasons[i] = Season.values()[buffer.get() & 0xFF];
                this.m_minTemperatures[i] = buffer.get();
                this.m_maxTemperatures[i] = buffer.get();
                this.m_avgWind[i] = (buffer.getShort() & 0xFFFF) / 65535.0f;
                this.m_avgPrecipitations[i] = (buffer.getShort() & 0xFFFF) / 65535.0f;
                this.m_modTemperatures[i] = buffer.get();
                this.m_modWind[i] = buffer.getShort() / 32767.0f;
                this.m_modPrecipitations[i] = buffer.getShort() / 32767.0f;
            }
            this.m_seasonTemperatureLimits = new HashMap<Season, float[]>();
            final int nbSeasons = buffer.get() & 0xFF;
            for (int j = 0; j < nbSeasons; ++j) {
                final Season season = Season.values()[buffer.get() & 0xFF];
                final float tMin = buffer.get();
                final float tMax = buffer.get();
                this.m_seasonTemperatureLimits.put(season, new float[] { tMin, tMax });
            }
            for (int left = 2 - nbSeasons, k = 0; k < left; ++k) {
                buffer.getShort();
                buffer.get();
            }
        }
    }
    
    public void reset() {
        this.m_historyLength = 0;
        this.m_seasons = null;
        this.m_minTemperatures = null;
        this.m_maxTemperatures = null;
        this.m_modTemperatures = null;
        this.m_avgWind = null;
        this.m_modWind = null;
        this.m_avgPrecipitations = null;
        this.m_modPrecipitations = null;
        this.m_seasonTemperatureLimits = null;
    }
}
