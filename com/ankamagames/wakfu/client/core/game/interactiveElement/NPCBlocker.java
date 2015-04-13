package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class NPCBlocker extends WakfuClientMapInteractiveElement
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
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.WALKON;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public byte getHeight() {
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NPCBlocker.class);
    }
    
    public static class NPCBlockerFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            NPCBlocker npcBlocker;
            try {
                npcBlocker = (NPCBlocker)NPCBlockerFactory.m_pool.borrowObject();
                npcBlocker.setPool(NPCBlockerFactory.m_pool);
            }
            catch (Exception e) {
                NPCBlocker.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                npcBlocker = new NPCBlocker();
            }
            return npcBlocker;
        }
        
        static {
            NPCBlockerFactory.m_pool = new MonitoredPool(new ObjectFactory<NPCBlocker>() {
                @Override
                public NPCBlocker makeObject() {
                    return new NPCBlocker();
                }
            });
        }
    }
}
