package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class BallotBox extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private static final int MRU_GFX_ID = 31;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case VOTE: {
                BallotBox.m_logger.info((Object)"[BallotBox] vote d'un candidat");
                this.sendActionMessage(action);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.VOTE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.VOTE };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.urne");
    }
    
    static {
        m_logger = Logger.getLogger((Class)BallotBox.class);
    }
    
    public static class BallotBoxFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            BallotBox machine;
            try {
                machine = (BallotBox)BallotBoxFactory.m_pool.borrowObject();
                machine.setPool(BallotBoxFactory.m_pool);
            }
            catch (Exception e) {
                BallotBox.m_logger.error((Object)"Erreur lors de l'extraction d'une GuildMachine du pool", (Throwable)e);
                machine = new BallotBox();
            }
            return machine;
        }
        
        static {
            BallotBoxFactory.m_pool = new MonitoredPool(new ObjectFactory<BallotBox>() {
                @Override
                public BallotBox makeObject() {
                    return new BallotBox();
                }
            });
        }
    }
}
