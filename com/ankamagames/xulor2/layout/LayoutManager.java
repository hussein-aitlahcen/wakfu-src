package com.ankamagames.xulor2.layout;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;

public interface LayoutManager extends Poolable
{
    Dimension getContentGreedySize(Container p0, Widget p1, Dimension p2);
    
    Dimension getContentPreferedSize(Container p0);
    
    Dimension getContentMinSize(Container p0);
    
    void layoutContainer(Container p0);
    
    void layoutWidget(Container p0, Widget p1);
    
    boolean isStandAlone();
}
