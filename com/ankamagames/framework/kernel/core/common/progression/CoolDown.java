package com.ankamagames.framework.kernel.core.common.progression;

public interface CoolDown
{
    long getStartTime();
    
    long getCoolDown();
    
    boolean onCoolDownExpired();
    
    void setStartTime(long p0);
}
