package com.ankamagames.wakfu.client.core.game.ressource;

import org.jetbrains.annotations.*;

public enum ResourceTemperatureInfluence
{
    EXTRA_COLD, 
    COLD, 
    HAPPY, 
    HOT, 
    EXTRA_HOT;
    
    @NotNull
    public static ResourceTemperatureInfluence getFromTemperature(final float currentTemp, final int extendedMin, final int idealMin, final int idealMax, final int extendedMax) {
        if (currentTemp < extendedMin) {
            return ResourceTemperatureInfluence.EXTRA_COLD;
        }
        if (currentTemp < idealMin) {
            return ResourceTemperatureInfluence.COLD;
        }
        if (currentTemp < idealMax) {
            return ResourceTemperatureInfluence.HAPPY;
        }
        if (currentTemp < extendedMax) {
            return ResourceTemperatureInfluence.HOT;
        }
        return ResourceTemperatureInfluence.EXTRA_HOT;
    }
}
