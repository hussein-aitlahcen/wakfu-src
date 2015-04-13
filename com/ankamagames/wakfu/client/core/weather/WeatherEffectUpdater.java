package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.wakfu.common.game.weather.*;
import java.util.*;

public interface WeatherEffectUpdater
{
    void setActivated(boolean p0);
    
    void setWindStrength(float p0);
    
    void setWindDirection(float p0);
    
    void setStrength(Weather p0, float p1);
    
    void updateParameters(long p0, FloatParameter p1, FloatParameter p2, WeatherEffect p3, HashMap<Weather, WeatherEffect> p4);
}
