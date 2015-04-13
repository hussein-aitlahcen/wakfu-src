package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle;

public interface PathMovementFactory<T extends PathMovementStyle>
{
    T create();
}
