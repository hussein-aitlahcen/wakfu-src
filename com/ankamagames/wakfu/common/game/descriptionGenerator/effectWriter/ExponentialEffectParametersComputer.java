package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ExponentialEffectParametersComputer extends DefaultEffectParametersComputer
{
    public static final EffectParametersComputer INSTANCE;
    public static final Logger m_logger;
    
    @Override
    Object[] getAsObjects(final WakfuEffect effect, final short containerLevel) {
        final Object[] params = super.getAsObjects(effect, containerLevel);
        if (effect.getParamsCount() < 3) {
            ExponentialEffectParametersComputer.m_logger.error((Object)("Unable to compute exponential parameters : need 3 values, only " + effect.getParamsCount() + " available"));
            return params;
        }
        final float a = effect.getParam(0, containerLevel);
        final float b = effect.getParam(1, containerLevel);
        final float c = effect.getParam(2, containerLevel);
        final double result = a + b * Math.pow(containerLevel, c);
        final double roundedResult = (b >= 0.0f) ? Math.floor(result) : Math.ceil(result);
        params[0] = new RoundedNumber((long)roundedResult, result);
        return params;
    }
    
    static {
        INSTANCE = new ExponentialEffectParametersComputer();
        m_logger = Logger.getLogger((Class)ExponentialEffectParametersComputer.class);
    }
}
