package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public final class EfficencyGenericEffect extends WakfuFightEffectImpl
{
    private static EfficencyGenericEffect m_instance;
    public static final int EFFICENCY_GENERIC_EFFECT_ACTION_ID = -2;
    public static final short EFFICENCY_STATE_ID = 72;
    
    public static EfficencyGenericEffect getInstance() {
        return EfficencyGenericEffect.m_instance;
    }
    
    public EfficencyGenericEffect() {
        super(-2, -2, AreaOfEffectEnum.POINT.newInstance(null, (short)0), EfficencyGenericEffect.INITIALIZED_TO_0_INT_ARRAY, EfficencyGenericEffect.EMPTY_INT_ARRAY, EfficencyGenericEffect.EMPTY_INT_ARRAY, EfficencyGenericEffect.EMPTY_INT_ARRAY, EfficencyGenericEffect.EMPTY_INT_ARRAY, EfficencyGenericEffect.EMPTY_INT_ARRAY, EfficencyGenericEffect.EMPTY_INT_ARRAY, 0L, null, false, 0, 0.0f, false, false, 0, 0.0f, EfficencyGenericEffect.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), true, false, true, false, null, true, true, true, false);
    }
    
    static {
        EfficencyGenericEffect.m_instance = new EfficencyGenericEffect();
    }
}
