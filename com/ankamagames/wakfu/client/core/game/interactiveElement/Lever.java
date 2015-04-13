package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Lever extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        Lever.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        Lever.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.runScript(action);
        switch (action) {
            case ACTIVATE: {
                this.setState((short)Math.max(1, (this.getState() + 1) % 3));
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
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public byte getHeight() {
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Lever.class);
    }
    
    public static class LeverFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Lever lever;
            try {
                lever = (Lever)LeverFactory.m_pool.borrowObject();
                lever.setPool(LeverFactory.m_pool);
            }
            catch (Exception e) {
                Lever.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                lever = new Lever();
            }
            return lever;
        }
        
        static {
            LeverFactory.m_pool = new MonitoredPool(new ObjectFactory<Lever>() {
                @Override
                public Lever makeObject() {
                    return new Lever();
                }
            });
        }
    }
}
