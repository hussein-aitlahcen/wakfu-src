package com.ankamagames.baseImpl.graphics.alea;

import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;

public interface MapLoader
{
    void initialize(EnvironmentMapManager p0, DisplayedScreenWorld p1, LightningMapManager p2);
    
    void loadMaps(short p0, short p1);
    
    void prepare(short p0);
    
    boolean acceptMap(short p0, short p1);
    
    void clear();
    
    void prepareAmbianceZone(short p0);
}
