package com.ankamagames.framework.kernel.core.common;

public interface Poolable
{
    void onCheckOut();
    
    void onCheckIn();
}
