package com.ankamagames.xulor2.core.graphicalMouse;

import com.ankamagames.xulor2.component.*;

public interface GraphicalMouseFactory<T extends GraphicalMouseData>
{
    Widget createWidget(T p0);
    
    void destroyWidget(Widget p0);
}
