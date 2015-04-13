package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import org.apache.log4j.*;

public abstract class WakfuDynamicElementTypeProvider implements DynamicElementTypeProvider
{
    protected static final Logger m_logger;
    
    static {
        m_logger = Logger.getLogger((Class)WakfuDynamicElementTypeProvider.class);
    }
}
