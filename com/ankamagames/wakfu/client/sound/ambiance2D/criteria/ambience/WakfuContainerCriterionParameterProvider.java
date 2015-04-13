package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class WakfuContainerCriterionParameterProvider implements ContainerCriterionParameterProvider
{
    @Override
    public Season getSeason() {
        return WeatherInfoManager.getInstance().getCurrentWeather().getSeason();
    }
    
    @Override
    public float getTemperature() {
        final WeatherInfo weatherInfo = WeatherInfoManager.getInstance().getCurrentWeather().getWeatherInfo();
        return (weatherInfo == null) ? 0.0f : weatherInfo.getTemperature();
    }
    
    @Override
    public float getWakfuScore() {
        return WakfuGlobalZoneManager.getInstance().getZoneEquilibrium();
    }
    
    @Override
    public float getTimeOfDay() {
        if (!WakfuGameCalendar.getInstance().isSunStatusComputed()) {
            return -1.0f;
        }
        final float value = WakfuGameCalendar.getInstance().getCurrentPhasePercentage() / 2.0f;
        return (value + (WakfuGameCalendar.getInstance().isSunShining() ? 0 : 50)) / 100.0f;
    }
    
    @Override
    public int getZoneTypeId() {
        final AmbienceData data = WakfuAmbianceListener.getInstance().getAmbianceData();
        if (data != null) {
            return data.m_zoneTypeId;
        }
        return -1;
    }
    
    @Override
    public int getNumPlayers() {
        return CharacterInfoManager.getInstance().getNumPlayerCharacters();
    }
    
    @Override
    public int getAltitude() {
        final float altitude = WorldInfoManager.getInstance().getInfo((short)TopologyMapManager.getWorldId()).m_altitude;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return (localPlayer != null) ? ((int)(localPlayer.getAltitude() - altitude)) : 0;
    }
}
