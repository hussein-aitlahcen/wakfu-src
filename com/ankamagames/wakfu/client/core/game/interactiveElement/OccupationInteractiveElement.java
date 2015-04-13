package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.skill.*;

public interface OccupationInteractiveElement
{
    long getId();
    
    short getState();
    
    Point3 getPosition();
    
    ActionVisual getVisual();
    
    void setState(short p0);
    
    void notifyViews();
    
    short getUsedState();
}
