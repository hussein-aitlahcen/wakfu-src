package com.ankamagames.wakfu.common.game.aptitudeNewVersion.listener;

import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import org.apache.log4j.*;

public final class AptitudeBonusModificationLogger implements AptitudeBonusInventoryListener
{
    private static final Logger m_logger;
    
    @Override
    public void onLevelChanged(final int bonusId, final short level) {
        AptitudeBonusModificationLogger.m_logger.info((Object)("Changement de niveau pour le bonus " + bonusId + ", nouveau level = " + level));
    }
    
    static {
        m_logger = Logger.getLogger((Class)AptitudeBonusModificationLogger.class);
    }
}
