package com.ankamagames.xulor2.core.dragndrop;

import com.ankamagames.xulor2.component.*;

public interface DragNDropHandler
{
    boolean isDndWidget(Widget p0, int p1, int p2);
    
    void select(int p0, int p1);
    
    void drag(int p0, int p1, Widget p2);
    
    void drop(int p0, int p1, Widget p2);
    
    void clean();
    
    boolean release();
    
    Object getValue();
}
