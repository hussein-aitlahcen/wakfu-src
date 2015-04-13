package com.ankamagames.wakfu.common.game.weather;

public enum Temperature
{
    COLD, 
    NORMAL, 
    HOT;
    
    public static final int MIN = -20;
    public static final int MAX = 40;
    
    public static Temperature getTemperature(final int tempValue) {
        if (tempValue < 0) {
            return Temperature.COLD;
        }
        if (tempValue > 20) {
            return Temperature.HOT;
        }
        return Temperature.NORMAL;
    }
}
