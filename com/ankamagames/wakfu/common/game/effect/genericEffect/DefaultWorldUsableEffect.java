package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class DefaultWorldUsableEffect extends WakfuStandardEffect
{
    private static final DefaultWorldUsableEffect m_instance;
    
    public static DefaultWorldUsableEffect getInstance() {
        return DefaultWorldUsableEffect.m_instance;
    }
    
    private DefaultWorldUsableEffect() {
        super(-7, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultWorldUsableEffect.EMPTY_INT_ARRAY, DefaultWorldUsableEffect.EMPTY_INT_ARRAY, DefaultWorldUsableEffect.EMPTY_INT_ARRAY, DefaultWorldUsableEffect.EMPTY_INT_ARRAY, DefaultWorldUsableEffect.EMPTY_INT_ARRAY, DefaultWorldUsableEffect.EMPTY_INT_ARRAY, DefaultWorldUsableEffect.EMPTY_INT_ARRAY, 0L, null, false, DefaultWorldUsableEffect.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), true, false, false, true, false, null, false, false, false);
    }
    
    static {
        m_instance = new DefaultWorldUsableEffect();
    }
}
