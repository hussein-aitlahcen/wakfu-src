package com.ankamagames.wakfu.common.game.effect.runningEffect;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class WakfuRunningEffectUIDGenerator implements RunningEffectUIDGenerator
{
    protected static final Logger m_logger;
    public static final long START_COUNTER = 16777216L;
    private static long m_counter;
    
    @Override
    public long getNextUID(final RunningEffect parameterizedRunningEffect) {
        if (parameterizedRunningEffect == null) {
            return -1L;
        }
        return this.getClassicNextUID();
    }
    
    private long getClassicNextUID() {
        if (WakfuRunningEffectUIDGenerator.m_counter < Long.MAX_VALUE) {
            return WakfuRunningEffectUIDGenerator.m_counter++;
        }
        return WakfuRunningEffectUIDGenerator.m_counter = 16777216L;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuRunningEffectUIDGenerator.class);
        WakfuRunningEffectUIDGenerator.m_counter = 16777216L;
    }
}
