package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DialogMachine extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action != InteractiveElementAction.ACTIVATE) {
            return false;
        }
        this.runScript(action);
        this.sendActionMessage(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return new AbstractMRUAction[] { MRUActions.INTERACTIF_ACTION.getMRUAction() };
    }
    
    @Override
    public String getName() {
        return "TODO Clem: Machine qui parle";
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_state = 1;
        this.setVisible(true);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.m_overHeadable = true;
        this.m_selectable = true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DialogMachine.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DialogMachine ie;
            try {
                ie = (DialogMachine)Factory.m_pool.borrowObject();
                ie.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                DialogMachine.m_logger.error((Object)"Erreur lors de l'extraction d'une CharacterStatue du pool", (Throwable)e);
                ie = new DialogMachine();
            }
            return ie;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<DialogMachine>() {
                @Override
                public DialogMachine makeObject() {
                    return new DialogMachine();
                }
            });
        }
    }
}
