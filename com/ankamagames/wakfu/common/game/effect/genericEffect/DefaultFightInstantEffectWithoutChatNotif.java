package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

final class DefaultFightInstantEffectWithoutChatNotif extends WakfuFightEffectImpl
{
    private static final DefaultFightInstantEffectWithoutChatNotif m_instance;
    
    public static DefaultFightInstantEffectWithoutChatNotif getInstance() {
        return DefaultFightInstantEffectWithoutChatNotif.m_instance;
    }
    
    private DefaultFightInstantEffectWithoutChatNotif() {
        super(-12, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, DefaultFightInstantEffectWithoutChatNotif.EMPTY_INT_ARRAY, 0L, null, false, 0, 0.0f, false, false, 0, 0.0f, DefaultFightInstantEffectWithoutChatNotif.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), false, false, true, false, null, false, false, false, false);
    }
    
    static {
        m_instance = new DefaultFightInstantEffectWithoutChatNotif();
    }
}
