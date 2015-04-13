package com.ankamagames.baseImpl.graphics;

public interface VisibleChangeable
{
    void fireVisibilityChanged(boolean p0, VisibleChangedListener.VisibleChangedCause p1);
    
    void addVisibleChangedListener(VisibleChangedListener p0);
    
    void removeVisibleChangedListener(VisibleChangedListener p0);
}
