package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Chest extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    public static final short OPENED = 0;
    public static final short CLOSED = 1;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)0);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setUseSpecificAnimTransition(true);
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        Chest.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        Chest.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.runScript(action);
        switch (action) {
            case OPEN: {
                if (this.getState() == 1) {
                    this.sendActionMessage(action);
                }
                return true;
            }
            case CLOSE: {
                this.sendActionMessage(action);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.OPEN;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.OPEN };
    }
    
    @Override
    public byte getHeight() {
        return 4;
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    static {
        m_logger = Logger.getLogger((Class)Chest.class);
    }
    
    public static class ChestFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Chest chest;
            try {
                chest = (Chest)ChestFactory.m_pool.borrowObject();
                chest.setPool(ChestFactory.m_pool);
            }
            catch (Exception e) {
                Chest.m_logger.error((Object)"Erreur lors de l'extraction d'un Chest du pool", (Throwable)e);
                chest = new Chest();
            }
            return chest;
        }
        
        static {
            ChestFactory.m_pool = new MonitoredPool(new ObjectFactory<Chest>() {
                @Override
                public Chest makeObject() {
                    return new Chest();
                }
            });
        }
    }
}
