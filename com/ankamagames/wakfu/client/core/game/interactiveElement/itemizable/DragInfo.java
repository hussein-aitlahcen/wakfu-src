package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.framework.kernel.core.maths.*;

public interface DragInfo
{
    boolean isBeingDragged();
    
    Point3 getDragPoint();
    
    void setDragPoint(int p0, int p1, short p2);
    
    void startDrag();
    
    void validateDrag();
    
    void cancelDrag();
    
    void clearCheckValidation();
    
    void clear();
}
