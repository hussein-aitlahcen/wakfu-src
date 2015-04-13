package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class StoppinPositionZoneTrigger extends ZoneTrigger
{
    private static final Logger m_logger;
    
    @Override
    protected void walkinActions(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (user instanceof PlayerCharacter) {
            ((PlayerCharacter)user).getActor().stopMoving();
        }
        super.walkinActions(action, user);
    }
    
    static {
        m_logger = Logger.getLogger((Class)StoppinPositionZoneTrigger.class);
    }
    
    public static class StoppinPositionZoneTriggerFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            StoppinPositionZoneTrigger stoppinPositionZoneTrigger;
            try {
                stoppinPositionZoneTrigger = (StoppinPositionZoneTrigger)StoppinPositionZoneTriggerFactory.m_pool.borrowObject();
                stoppinPositionZoneTrigger.setPool(StoppinPositionZoneTriggerFactory.m_pool);
            }
            catch (Exception e) {
                StoppinPositionZoneTrigger.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                stoppinPositionZoneTrigger = new StoppinPositionZoneTrigger(null);
            }
            return stoppinPositionZoneTrigger;
        }
        
        static {
            StoppinPositionZoneTriggerFactory.m_pool = new MonitoredPool(new ObjectFactory<StoppinPositionZoneTrigger>() {
                @Override
                public StoppinPositionZoneTrigger makeObject() {
                    return new StoppinPositionZoneTrigger(null);
                }
            });
        }
    }
}
