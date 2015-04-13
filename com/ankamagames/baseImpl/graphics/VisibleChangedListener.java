package com.ankamagames.baseImpl.graphics;

public interface VisibleChangedListener
{
    void onVisibleChanged(boolean p0, VisibleChangedCause p1);
    
    public enum VisibleChangedCause
    {
        LAYER, 
        VISIBLE, 
        FIGHT;
    }
}
