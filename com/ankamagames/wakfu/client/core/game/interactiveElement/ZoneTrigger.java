package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ZoneTrigger extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private boolean m_localPlayerWasInside;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        ZoneTrigger.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case WALKIN: {
                if (this.m_state == 1 && !this.m_localPlayerWasInside) {
                    ZoneTrigger.m_logger.info((Object)("Action [" + action + "] performed on interactive element : " + this.getId()));
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventWalkinZoneTrigger(this.getId()));
                    this.walkinActions(action, user);
                }
                else {
                    ZoneTrigger.m_logger.info((Object)("Action [" + action + "] ignored on interactive element : " + this.getId()));
                }
                return this.m_localPlayerWasInside = true;
            }
            case WALKOUT: {
                if (this.m_state == 1 && this.m_localPlayerWasInside) {
                    ZoneTrigger.m_logger.info((Object)("Action [" + action + "] performed on interactive element : " + this.getId()));
                    this.walkoutActions(action, user);
                }
                else {
                    ZoneTrigger.m_logger.info((Object)("Action [" + action + "] ignored on interactive element : " + this.getId()));
                }
                this.m_localPlayerWasInside = false;
                return true;
            }
            default: {
                ZoneTrigger.m_logger.info((Object)("Action [" + action + "] not processed on interactive element : " + this.getId()));
                return false;
            }
        }
    }
    
    protected void walkoutActions(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.commonActions(action, user);
    }
    
    protected void walkinActions(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.commonActions(action, user);
    }
    
    protected void commonActions(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        this.notifyViews();
        this.sendActionMessage(action);
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.WALKIN;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.WALKIN, InteractiveElementAction.WALKOUT, InteractiveElementAction.WALKON };
    }
    
    public boolean isLocalPlayerWasInside() {
        return this.m_localPlayerWasInside;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_localPlayerWasInside = false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZoneTrigger.class);
    }
    
    public static class ZoneTriggerFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            ZoneTrigger zoneTrigger;
            try {
                zoneTrigger = (ZoneTrigger)ZoneTriggerFactory.m_pool.borrowObject();
                zoneTrigger.setPool(ZoneTriggerFactory.m_pool);
            }
            catch (Exception e) {
                ZoneTrigger.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                zoneTrigger = new ZoneTrigger();
            }
            return zoneTrigger;
        }
        
        static {
            ZoneTriggerFactory.m_pool = new MonitoredPool(new ObjectFactory<ZoneTrigger>() {
                @Override
                public ZoneTrigger makeObject() {
                    return new ZoneTrigger();
                }
            });
        }
    }
}
