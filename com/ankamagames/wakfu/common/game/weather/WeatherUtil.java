package com.ankamagames.wakfu.common.game.weather;

import com.ankamagames.baseImpl.common.clientAndServer.world.climate.*;

public final class WeatherUtil
{
    public static final float STORM_WIND = 0.9f;
    
    public static Weather fromParams(final float rainIntensity, final float temperature, final float wind) {
        final Humidity h = Humidity.getFromRainIntensity(rainIntensity);
        if (h == Humidity.DRY) {
            return Weather.SUNNY;
        }
        final Temperature t = Temperature.getTemperature(Math.round(temperature));
        if (h == Humidity.HUMID) {
            if (t == Temperature.COLD) {
                return Weather.FOGGY;
            }
            return Weather.CLOUDY;
        }
        else {
            if (t == Temperature.COLD) {
                return (wind > 0.9f) ? Weather.BLIZZARD : Weather.SNOW;
            }
            return (wind > 0.9f) ? Weather.STORM : Weather.RAIN;
        }
    }
    
    public static Weather fromParams(final DailyWeather w) {
        return fromParams(w.getCurrentRainEventIntensity(), w.getCurrentTemperature(), w.getCurrentWind());
    }
}
