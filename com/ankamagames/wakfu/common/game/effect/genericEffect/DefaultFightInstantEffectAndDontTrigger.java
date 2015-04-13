package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class DefaultFightInstantEffectAndDontTrigger extends WakfuFightEffectImpl
{
    private static final DefaultFightInstantEffectAndDontTrigger m_instance;
    
    public static DefaultFightInstantEffectAndDontTrigger getInstance() {
        return DefaultFightInstantEffectAndDontTrigger.m_instance;
    }
    
    private DefaultFightInstantEffectAndDontTrigger() {
        super(-8, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, DefaultFightInstantEffectAndDontTrigger.EMPTY_INT_ARRAY, 0L, null, false, 0, 0.0f, false, false, 0, 0.0f, DefaultFightInstantEffectAndDontTrigger.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), false, true, true, false, null, false, false, false, false);
    }
    
    static {
        m_instance = new DefaultFightInstantEffectAndDontTrigger();
    }
}
