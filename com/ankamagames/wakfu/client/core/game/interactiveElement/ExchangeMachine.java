package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ExchangeMachine extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEExchangeParameter m_param;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action != InteractiveElementAction.ACTIVATE) {
            return false;
        }
        this.runScript(action);
        UIExchangeMachineFrame.getInstance().setExchangeMachine(this);
        this.sendActionMessage(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        if (this.m_param.getId() == 64 && WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return null;
        }
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        if (this.m_param.getId() == 64 && WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return InteractiveElementAction.EMPTY_ACTIONS;
        }
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public void initializeWithParameter() {
        this.m_param = (IEExchangeParameter)IEParametersManager.INSTANCE.getParam(IETypes.EXCHANGE_MACHINE, Integer.parseInt(this.m_parameter));
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.m_param.getId() == 64 && WakfuGameCalendar.getInstance().getDate().before(ActivationConstants.KROZMASTER_UNLOCK_DATE)) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_param.getVisualId());
        action.setGfxId(visual.getMruGfx());
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(107, (this.m_param == null) ? -1 : this.m_param.getId(), new Object[0]);
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
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new ExchangeMachineItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    public IEExchangeParameter getParam() {
        return this.m_param;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExchangeMachine.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            ExchangeMachine ie;
            try {
                ie = (ExchangeMachine)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                ExchangeMachine.m_logger.error((Object)"Erreur lors de l'extraction d'une CharacterStatue du pool", (Throwable)e);
                ie = new ExchangeMachine();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<ExchangeMachine>() {
                @Override
                public ExchangeMachine makeObject() {
                    return new ExchangeMachine();
                }
            });
        }
    }
}
