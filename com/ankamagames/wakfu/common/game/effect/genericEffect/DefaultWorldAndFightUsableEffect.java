package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class DefaultWorldAndFightUsableEffect extends WakfuStandardEffect
{
    private static final DefaultWorldAndFightUsableEffect m_instance;
    
    public static DefaultWorldAndFightUsableEffect getInstance() {
        return DefaultWorldAndFightUsableEffect.m_instance;
    }
    
    private DefaultWorldAndFightUsableEffect() {
        super(-6, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, DefaultWorldAndFightUsableEffect.EMPTY_INT_ARRAY, 0L, null, false, DefaultWorldAndFightUsableEffect.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), true, false, true, true, false, null, false, false, false);
    }
    
    static {
        m_instance = new DefaultWorldAndFightUsableEffect();
    }
}
