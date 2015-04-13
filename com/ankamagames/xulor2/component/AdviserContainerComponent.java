package com.ankamagames.xulor2.component;

public interface AdviserContainerComponent
{
    int validateAdviser();
    
    void onAdviserCleanUp();
    
    int getXOffset();
    
    int getYOffset();
    
    float getWorldX();
    
    float getWorldY();
    
    float getAltitude();
}
