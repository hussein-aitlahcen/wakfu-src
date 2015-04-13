package com.ankamagames.framework.kernel.core.common;

public final class CurrentTimeMillisTimeProvider implements TimeProvider
{
    public static final CurrentTimeMillisTimeProvider INSTANCE;
    
    @Override
    public long current() {
        return System.currentTimeMillis();
    }
    
    static {
        INSTANCE = new CurrentTimeMillisTimeProvider();
    }
}
