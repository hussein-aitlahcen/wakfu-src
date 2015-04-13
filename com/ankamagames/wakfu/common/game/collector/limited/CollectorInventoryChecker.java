package com.ankamagames.wakfu.common.game.collector.limited;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.item.*;

public abstract class CollectorInventoryChecker
{
    protected static final Logger m_logger;
    protected final IECollectorParameter m_param;
    
    protected CollectorInventoryChecker(final IECollectorParameter param) {
        super();
        this.m_param = param;
    }
    
    public abstract boolean canAddCash(final Wallet p0, final int p1);
    
    public abstract boolean canSubCash(final Wallet p0, final int p1);
    
    static {
        m_logger = Logger.getLogger((Class)CollectorInventoryChecker.class);
    }
}
