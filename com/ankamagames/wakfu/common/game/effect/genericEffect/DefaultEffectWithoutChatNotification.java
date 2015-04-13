package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public final class DefaultEffectWithoutChatNotification extends WakfuStandardEffect
{
    private static final DefaultEffectWithoutChatNotification m_instance;
    
    public static DefaultEffectWithoutChatNotification getInstance() {
        return DefaultEffectWithoutChatNotification.m_instance;
    }
    
    private DefaultEffectWithoutChatNotification() {
        super(-10, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, DefaultEffectWithoutChatNotification.EMPTY_INT_ARRAY, 0L, null, false, DefaultEffectWithoutChatNotification.EMPTY_FLOAT_ARRAY, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), false, false, false, false, false, null, false, false, false);
    }
    
    static {
        m_instance = new DefaultEffectWithoutChatNotification();
    }
}
