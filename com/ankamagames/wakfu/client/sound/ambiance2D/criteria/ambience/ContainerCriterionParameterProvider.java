package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface ContainerCriterionParameterProvider
{
    Season getSeason();
    
    float getTemperature();
    
    float getWakfuScore();
    
    float getTimeOfDay();
    
    int getZoneTypeId();
    
    int getNumPlayers();
    
    int getAltitude();
}
