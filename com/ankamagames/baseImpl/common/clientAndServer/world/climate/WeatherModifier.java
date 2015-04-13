package com.ankamagames.baseImpl.common.clientAndServer.world.climate;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class WeatherModifier
{
    private final ClimateBonus m_parameters;
    private final GameDateConst m_date;
    
    public WeatherModifier(final ClimateBonus parameters, final GameDateConst date) {
        super();
        this.m_parameters = parameters;
        this.m_date = new GameDate(date);
    }
    
    public float precipitations() {
        return this.m_parameters.getPrecipitations();
    }
    
    public float temperature() {
        return this.m_parameters.getTemperature();
    }
    
    public float wind() {
        return this.m_parameters.getWind();
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    public ClimateBonus getParameters() {
        return this.m_parameters;
    }
}
