package com.ankamagames.framework.graphics.engine.test;

public interface HardwareFeatureTest
{
    void initialize();
    
    boolean runTest();
    
    void cleanUp();
    
    HardwareFeature getFeature();
}
