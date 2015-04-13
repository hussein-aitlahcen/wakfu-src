package com.ankamagames.baseImpl.graphics.alea.mobile;

import com.ankamagames.baseImpl.graphics.isometric.*;

public interface TargetPositionListener<T extends IsoWorldTarget>
{
    void cellPositionChanged(T p0, int p1, int p2, short p3);
}
