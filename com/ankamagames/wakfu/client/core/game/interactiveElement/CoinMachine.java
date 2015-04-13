package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CoinMachine extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case START_MANAGE: {
                final ManageCoinMachineOccupation occupation = new ManageCoinMachineOccupation(this);
                if (!occupation.isAllowed()) {
                    CoinMachine.m_logger.error((Object)"[COIN_MACHINE] Impossible de d\u00e9marrer le browsing");
                    return false;
                }
                occupation.begin();
                this.sendActionMessage(action);
            }
            case STOP_MANAGE: {
                this.runScript(action);
                this.sendActionMessage(action);
                WakfuGameEntity.getInstance().getLocalPlayer().finishCurrentOccupation();
                return true;
            }
            case START_BROWSING: {
                this.runScript(action);
                this.sendActionMessage(action);
                return true;
            }
            case STOP_BROWSING: {
                this.runScript(action);
                this.sendActionMessage(action);
                return true;
            }
            case ACTIVATE: {
                this.runScript(action);
                this.sendActionMessage(action);
                return true;
            }
            default: {
                CoinMachine.m_logger.error((Object)("Impossible d'effectuer l'action " + action), (Throwable)new IllegalArgumentException());
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.START_BROWSING;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_MANAGE, InteractiveElementAction.STOP_MANAGE, InteractiveElementAction.START_BROWSING, InteractiveElementAction.ACTIVATE, InteractiveElementAction.STOP_BROWSING };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
    }
    
    @Override
    public boolean checkSubscription() {
        return false;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return new AbstractMRUAction[] { MRUActions.INTERACTIF_ACTION.getMRUAction() };
    }
    
    @Override
    public String getName() {
        return "TODO Clem: Bandit-manchot";
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)CoinMachine.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            CoinMachine ie;
            try {
                ie = (CoinMachine)Factory.m_pool.borrowObject();
                ie.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                CoinMachine.m_logger.error((Object)"Erreur lors de l'extraction du pool", (Throwable)e);
                ie = new CoinMachine();
            }
            return ie;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<CoinMachine>() {
                @Override
                public CoinMachine makeObject() {
                    return new CoinMachine();
                }
            });
        }
    }
}
