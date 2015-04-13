package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class DefaultEffectParametersComputer extends EffectParametersComputer
{
    public static final EffectParametersComputer INSTANCE;
    public static final Logger m_logger;
    
    @Override
    Object[] getAsObjects(final WakfuEffect effect, final short containerLevel) {
        final int effectParamsCount = effect.getParamsCount();
        final Object[] params = new Object[effectParamsCount];
        for (int j = 0; j < effectParamsCount; ++j) {
            final float rawValue = effect.getParam(j, containerLevel);
            final int roundedValue = effect.getParam(j, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            params[j] = new RoundedNumber(roundedValue, rawValue);
        }
        return params;
    }
    
    static {
        INSTANCE = new DefaultEffectParametersComputer();
        m_logger = Logger.getLogger((Class)DefaultEffectParametersComputer.class);
    }
}
