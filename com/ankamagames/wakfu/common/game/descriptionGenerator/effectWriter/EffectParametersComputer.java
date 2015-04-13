package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class EffectParametersComputer
{
    public static final Logger m_logger;
    
    abstract Object[] getAsObjects(final WakfuEffect p0, final short p1);
    
    static {
        m_logger = Logger.getLogger((Class)EffectParametersComputer.class);
    }
}
