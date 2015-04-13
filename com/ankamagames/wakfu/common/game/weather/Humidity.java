package com.ankamagames.wakfu.common.game.weather;

public enum Humidity
{
    DRY, 
    HUMID, 
    VERY_HUMID;
    
    public static Humidity getFromRainIntensity(final float intensity) {
        if (intensity < 0.334f) {
            return Humidity.DRY;
        }
        if (intensity < 0.667f) {
            return Humidity.HUMID;
        }
        return Humidity.VERY_HUMID;
    }
}
