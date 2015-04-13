package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PositionTrigger extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(false);
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        PositionTrigger.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case WALKON: {
                if (this.m_state == 2) {
                    PositionTrigger.m_logger.info((Object)("Action [" + action + "] ignored on interactive element : " + this.getId()));
                    return true;
                }
                PositionTrigger.m_logger.info((Object)("Action [" + action + "] performed on interactive element : " + this.getId()));
                this.runScript(action);
                this.notifyViews();
                this.sendActionMessage(action);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.WALKON;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.WALKON };
    }
    
    @Override
    public byte getHeight() {
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PositionTrigger.class);
    }
    
    public static class PositionTriggerFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            PositionTrigger positionTrigger;
            try {
                positionTrigger = (PositionTrigger)PositionTriggerFactory.m_pool.borrowObject();
                positionTrigger.setPool(PositionTriggerFactory.m_pool);
            }
            catch (Exception e) {
                PositionTrigger.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                positionTrigger = new PositionTrigger();
            }
            return positionTrigger;
        }
        
        static {
            PositionTriggerFactory.m_pool = new MonitoredPool(new ObjectFactory<PositionTrigger>() {
                @Override
                public PositionTrigger makeObject() {
                    return new PositionTrigger();
                }
            });
        }
    }
}
