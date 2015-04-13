package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class ContainerCriterionParameterManager implements ContainerCriterionParameterProvider
{
    private static final ContainerCriterionParameterManager m_instance;
    private ContainerCriterionParameterProvider m_provider;
    
    public static ContainerCriterionParameterManager getInstance() {
        return ContainerCriterionParameterManager.m_instance;
    }
    
    public void setProvider(final ContainerCriterionParameterProvider provider) {
        this.m_provider = provider;
    }
    
    @Override
    public Season getSeason() {
        return this.m_provider.getSeason();
    }
    
    @Override
    public float getTemperature() {
        return this.m_provider.getTemperature();
    }
    
    @Override
    public float getTimeOfDay() {
        return this.m_provider.getTimeOfDay();
    }
    
    @Override
    public int getZoneTypeId() {
        return this.m_provider.getZoneTypeId();
    }
    
    @Override
    public float getWakfuScore() {
        return this.m_provider.getWakfuScore();
    }
    
    @Override
    public int getNumPlayers() {
        return this.m_provider.getNumPlayers();
    }
    
    @Override
    public int getAltitude() {
        return this.m_provider.getAltitude();
    }
    
    static {
        m_instance = new ContainerCriterionParameterManager();
    }
}
