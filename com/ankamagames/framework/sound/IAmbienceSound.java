package com.ankamagames.framework.sound;

public interface IAmbienceSound
{
    int getId();
    
    long getFileId();
    
    boolean getStereo();
    
    float getMaxGain();
    
    float getMaxDistance();
    
    int getMaxTimeBeforeLoop();
    
    float getRollOffFactor();
    
    float getRefDistance();
    
    int getMinTimeBeforeLoop();
}
