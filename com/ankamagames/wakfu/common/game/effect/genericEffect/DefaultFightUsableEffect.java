package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class DefaultFightUsableEffect extends WakfuStandardEffect
{
    private static final int DEFAULT_MAX_LEVEL = 200;
    private static final DefaultFightUsableEffect m_instance;
    private static final DefaultFightUsableEffect m_instanceWithoutMaxLevel;
    
    public static DefaultFightUsableEffect getInstance() {
        return DefaultFightUsableEffect.m_instance;
    }
    
    public static DefaultFightUsableEffect getInstanceWithoutMaxLevel() {
        return DefaultFightUsableEffect.m_instanceWithoutMaxLevel;
    }
    
    private DefaultFightUsableEffect(final int containerMaxLevel) {
        super(-5, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultFightUsableEffect.EMPTY_INT_ARRAY, DefaultFightUsableEffect.EMPTY_INT_ARRAY, DefaultFightUsableEffect.EMPTY_INT_ARRAY, DefaultFightUsableEffect.EMPTY_INT_ARRAY, DefaultFightUsableEffect.EMPTY_INT_ARRAY, DefaultFightUsableEffect.EMPTY_INT_ARRAY, DefaultFightUsableEffect.EMPTY_INT_ARRAY, 0L, null, false, DefaultFightUsableEffect.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, containerMaxLevel, null, (short)(-1), 0.0f, (byte)(-1), true, false, true, false, false, null, false, false, false);
    }
    
    static {
        m_instance = new DefaultFightUsableEffect(200);
        m_instanceWithoutMaxLevel = new DefaultFightUsableEffect(Integer.MAX_VALUE);
    }
}
