package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagExitTrigger extends WakfuClientMapInteractiveElement
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
        DimensionalBagExitTrigger.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        DimensionalBagExitTrigger.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        switch (action) {
            case WALKON: {
                if (this.m_state == 1) {
                    this.runScript(action);
                    this.notifyViews();
                    WakfuClientInstance.getInstance();
                    final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
                    localPlayer.getVisitingDimentionalBag().leave();
                    return true;
                }
                break;
            }
        }
        return false;
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
        m_logger = Logger.getLogger((Class)DimensionalBagExitTrigger.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DimensionalBagExitTrigger trigger;
            try {
                trigger = (DimensionalBagExitTrigger)Factory.m_pool.borrowObject();
                trigger.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagExitTrigger.m_logger.error((Object)"Erreur lors de l'extraction d'un DimensionalBagExitTrigger du pool", (Throwable)e);
                trigger = new DimensionalBagExitTrigger();
            }
            return trigger;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagExitTrigger>() {
                @Override
                public DimensionalBagExitTrigger makeObject() {
                    return new DimensionalBagExitTrigger();
                }
            });
        }
    }
}
