package com.ankamagames.wakfu.common.game.effect.genericEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class DefaultFightOneTurnEffect extends WakfuFightEffectImpl
{
    public static final DefaultFightOneTurnEffect m_instance;
    private static final boolean DEFAULT_IS_DURATION_IN_FULL_TURNS = false;
    private static final boolean DEFAULT_ENDS_AT_END_OF_TURN = true;
    
    public static DefaultFightOneTurnEffect getInstance() {
        return DefaultFightOneTurnEffect.m_instance;
    }
    
    private DefaultFightOneTurnEffect() {
        this(false, true, DefaultFightOneTurnEffect.EMPTY_FLOAT_ARRAY);
    }
    
    private DefaultFightOneTurnEffect(final boolean isDurationInFullTurns, final boolean endsAtEndOfTurn, final float[] params) {
        super(-9, -1, AreaOfEffectEnum.POINT.newInstance(null, (short)0), DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, DefaultFightOneTurnEffect.EMPTY_INT_ARRAY, 0L, null, false, 1, 0.0f, endsAtEndOfTurn, isDurationInFullTurns, 0, 0.0f, params, 100.0f, 0.0f, false, false, false, false, 0, Integer.MAX_VALUE, null, (short)(-1), 0.0f, (byte)(-1), false, true, true, false, null, false, false, false, false);
    }
    
    public static DefaultFightOneTurnEffect makeWithParams(final float[] params) {
        return new DefaultFightOneTurnEffect(false, true, params);
    }
    
    public static DefaultFightOneTurnEffect makeWithTurnDetails(final boolean isDurationInFullTurns, final boolean endsAtEndOfTurn) {
        return new DefaultFightOneTurnEffect(isDurationInFullTurns, endsAtEndOfTurn, DefaultFightOneTurnEffect.EMPTY_FLOAT_ARRAY);
    }
    
    static {
        m_instance = new DefaultFightOneTurnEffect();
    }
}
