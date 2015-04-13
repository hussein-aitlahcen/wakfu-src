package com.ankamagames.wakfu.common.game.xp.synthlevel;

public interface SyntheticLevel
{
    public static final double CONSTANT_W = 7.0;
    public static final double CONSTANT_B = 20.0;
    
    double getSyntheticLevel();
    
    double getAverageLevel();
    
    double getSyntheticNumber();
    
    int getSize();
    
    double getContribution(short p0);
    
    int getTotalLevel();
}
