package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class StoppingPositionTrigger extends PositionTrigger
{
    private static final Logger m_logger;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        StoppingPositionTrigger.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        switch (action) {
            case WALKON: {
                if (this.m_state != 1) {
                    return true;
                }
                if (user instanceof PlayerCharacter) {
                    ((PlayerCharacter)user).getActor().stopMoving();
                }
                this.runScript(action);
                this.sendActionMessage(action);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)StoppingPositionTrigger.class);
    }
    
    public static class StoppingPositionTriggerFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            StoppingPositionTrigger positionTrigger;
            try {
                positionTrigger = (StoppingPositionTrigger)StoppingPositionTriggerFactory.m_pool.borrowObject();
                positionTrigger.setPool(StoppingPositionTriggerFactory.m_pool);
            }
            catch (Exception e) {
                StoppingPositionTrigger.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                positionTrigger = new StoppingPositionTrigger();
            }
            return positionTrigger;
        }
        
        static {
            StoppingPositionTriggerFactory.m_pool = new MonitoredPool(new ObjectFactory<StoppingPositionTrigger>() {
                @Override
                public StoppingPositionTrigger makeObject() {
                    return new StoppingPositionTrigger();
                }
            });
        }
    }
}
