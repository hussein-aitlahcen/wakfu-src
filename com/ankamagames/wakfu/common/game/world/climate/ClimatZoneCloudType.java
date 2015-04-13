package com.ankamagames.wakfu.common.game.world.climate;

public enum ClimatZoneCloudType
{
    CLEAR(0, 50.0), 
    CLOUDLESS(1, 100.0), 
    CLOUDY(2, 150.0), 
    VERY_CLOUDY(3, 200.0), 
    COVER(4, 250.0);
    
    public static final int NB_TYPE = 5;
    private int m_index;
    private double m_value;
    
    private ClimatZoneCloudType(final int index, final double value) {
        this.m_index = index;
        this.m_value = value;
    }
    
    public int getIndex() {
        return this.m_index;
    }
    
    public double getValue() {
        return this.m_value;
    }
    
    public static ClimatZoneCloudType getType(final int i) {
        switch (i) {
            case 0: {
                return ClimatZoneCloudType.CLEAR;
            }
            case 1: {
                return ClimatZoneCloudType.CLOUDLESS;
            }
            case 2: {
                return ClimatZoneCloudType.CLOUDY;
            }
            case 3: {
                return ClimatZoneCloudType.VERY_CLOUDY;
            }
            case 4: {
                return ClimatZoneCloudType.COVER;
            }
            default: {
                return ClimatZoneCloudType.CLEAR;
            }
        }
    }
}
