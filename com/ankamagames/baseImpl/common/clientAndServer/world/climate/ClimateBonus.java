package com.ankamagames.baseImpl.common.clientAndServer.world.climate;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class ClimateBonus
{
    public static final long DURATION_UNIT_IN_MS = 86400000L;
    private final int m_id;
    private final float m_temperature;
    private final float m_wind;
    private final float m_precipitations;
    private final SimpleCriterion m_criterion;
    private final int m_duration;
    private final short m_price;
    
    public ClimateBonus(final int id, final float temperature, final float wind, final float precipitations, final SimpleCriterion criterion, final int duration, final short price) {
        super();
        this.m_id = id;
        this.m_temperature = temperature;
        this.m_wind = wind;
        this.m_precipitations = precipitations;
        this.m_criterion = criterion;
        this.m_duration = duration;
        this.m_price = price;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public float getTemperature() {
        return this.m_temperature;
    }
    
    public float getWind() {
        return this.m_wind;
    }
    
    public float getPrecipitations() {
        return this.m_precipitations;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public short getPrice() {
        return this.m_price;
    }
}
