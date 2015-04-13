package com.ankamagames.xulor2.core.dragndrop;

import com.ankamagames.xulor2.component.*;

public interface DragNDropListener
{
    void onDrag(DragNDropHandler p0, int p1, int p2, Widget p3);
    
    void onDrop(DragNDropHandler p0, int p1, int p2, Widget p3);
    
    boolean validateContent(Object p0);
}
