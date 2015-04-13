package com.ankamagames.framework.ai.targetfinder;

public interface WorldPositionable
{
    float getWorldX();
    
    float getWorldY();
    
    float getAltitude();
    
    int getWorldCellX();
    
    int getWorldCellY();
    
    short getWorldCellAltitude();
}
