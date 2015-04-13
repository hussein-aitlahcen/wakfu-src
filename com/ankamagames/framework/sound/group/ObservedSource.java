package com.ankamagames.framework.sound.group;

public interface ObservedSource
{
    float getObservedX();
    
    float getObservedY();
    
    float getObservedZ();
    
    boolean isPositionRelative();
    
    int getGroupInstanceId();
}
