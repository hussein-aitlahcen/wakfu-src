package com.ankamagames.baseImpl.graphics.alea.display.lights;

public interface LitSceneObject
{
    int getWorldCellX();
    
    int getWorldCellY();
    
    short getWorldCellAltitude();
    
    short getLayerId();
    
    void applyLighting(float[] p0);
}
