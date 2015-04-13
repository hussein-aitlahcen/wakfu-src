package com.ankamagames.baseImpl.graphics.isometric.maskableLayer;

public interface Maskable
{
    int getMaskKey();
    
    short getLayerId();
    
    void setMaskKey(int p0, short p1);
}
