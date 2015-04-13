package com.ankamagames.wakfu.common.constants;

import java.util.*;

public final class EffectTriggersResetingLimitingCounter
{
    private static final BitSet TRIGGERS_SET;
    
    public static boolean containsResetingTrigger(final BitSet triggers) {
        return EffectTriggersResetingLimitingCounter.TRIGGERS_SET.intersects(triggers);
    }
    
    static {
        (TRIGGERS_SET = new BitSet()).set(1028);
        EffectTriggersResetingLimitingCounter.TRIGGERS_SET.set(1003);
        EffectTriggersResetingLimitingCounter.TRIGGERS_SET.set(1001);
        EffectTriggersResetingLimitingCounter.TRIGGERS_SET.set(1002);
    }
}
