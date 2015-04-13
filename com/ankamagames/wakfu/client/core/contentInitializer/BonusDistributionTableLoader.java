package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;

public final class BonusDistributionTableLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final BonusDistributionTableLoader m_instance;
    
    public static BonusDistributionTableLoader getInstance() {
        return BonusDistributionTableLoader.m_instance;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.bonusPointDistributionTable");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        clientInstance.fireContentInitializerDone(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BonusDistributionTableLoader.class);
        m_instance = new BonusDistributionTableLoader();
    }
}
