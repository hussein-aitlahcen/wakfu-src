package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class GuildLadder extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
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
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        this.setUseSpecificAnimTransition(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(false);
        this.setOverHeadable(false);
        this.setUseSpecificAnimTransition(false);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(MRUGfxConstants.HAND.m_id);
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("guild.ladder");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildLadder.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            GuildLadder ie;
            try {
                ie = (GuildLadder)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                GuildLadder.m_logger.error((Object)"Erreur lors de l'extraction du pool", (Throwable)e);
                ie = new GuildLadder();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new PoolFactory());
        }
    }
    
    private static class PoolFactory extends ObjectFactory<GuildLadder>
    {
        @Override
        public GuildLadder makeObject() {
            return new GuildLadder();
        }
    }
}
