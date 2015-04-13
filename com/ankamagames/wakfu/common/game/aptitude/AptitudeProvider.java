package com.ankamagames.wakfu.common.game.aptitude;

import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class AptitudeProvider implements InventoryContentProvider<Aptitude, RawAptitude>
{
    protected static final Logger m_logger;
    private static final AptitudeProvider m_instance;
    
    public static AptitudeProvider getInstance() {
        return AptitudeProvider.m_instance;
    }
    
    @Override
    public Aptitude unSerializeContent(final RawAptitude rawItem) {
        final ReferenceAptitude aptitudeType = ReferenceAptitudeManager.getInstance().getFromReferenceId(rawItem.referenceId);
        if (aptitudeType != null) {
            final Aptitude aptitude = new Aptitude(aptitudeType);
            aptitude.setLevel(rawItem.level);
            aptitude.setWonLevel(rawItem.wonLevel);
            return aptitude;
        }
        AptitudeProvider.m_logger.error((Object)("Aucune aptitude de referenceId=" + rawItem.referenceId));
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AptitudeProvider.class);
        m_instance = new AptitudeProvider();
    }
}
