package com.ankamagames.wakfu.client.updater;

public interface IComponentManager
{
    boolean hasComponentsCompleted(Component p0);
    
    void updateProgessInformation(double p0, int p1);
    
    void updateComponentInformation(String p0, int p1, boolean p2);
    
    void changeState(State p0);
}
