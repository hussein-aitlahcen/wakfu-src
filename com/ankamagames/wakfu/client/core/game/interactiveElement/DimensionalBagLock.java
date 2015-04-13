package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagLock extends WakfuClientMapInteractiveElement
{
    protected static final Logger m_logger;
    private static final byte LOCKED_STATE = 0;
    private static final byte UNLOCKED_STATE = 1;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        DimensionalBagLock.m_logger.info((Object)("Action performed on interactive element : " + action.toString()));
        this.sendActionMessage(action);
        final boolean doLock = this.getState() == 1;
        final DimensionalBagLockRequest message = new DimensionalBagLockRequest();
        message.setLockRequest(doLock);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        WakfuSoundManager.getInstance().playGUISound(doLock ? 600143L : 600142L);
        this.runScript(action);
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
    public AbstractMRUAction[] getMRUActions() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean ownBag = player.getVisitingDimentionalBag() == player.getOwnedDimensionalBag();
        final MRUGenericInteractiveAction action = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        action.setActionToExecute(InteractiveElementAction.ACTIVATE);
        if (this.getState() == 0) {
            action.setGfxId(MRUGfxConstants.BARREL_OPENED.m_id);
            action.setName("desc.mru.dimensionalBag.unlock");
        }
        else {
            action.setGfxId(MRUGfxConstants.BARREL_CLOSED.m_id);
            action.setName("desc.mru.dimensionalBag.lock");
        }
        action.setEnabled(ownBag);
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public short getMRUHeight() {
        return (short)(this.getHeight() * 10.0f);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)0);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setUseSpecificAnimTransition(true);
        this.setUsingSpecificOldState(false);
        this.setUsingDespawnAnimation(true);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("bleh");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagLock.class);
    }
    
    public static class DimensionalBagLockFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public DimensionalBagLock makeObject() {
            DimensionalBagLock element;
            try {
                element = (DimensionalBagLock)DimensionalBagLockFactory.m_pool.borrowObject();
                element.setPool(DimensionalBagLockFactory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagLock.m_logger.error((Object)"Erreur lors de l'extraction d'un LootChest du pool", (Throwable)e);
                element = new DimensionalBagLock();
            }
            return element;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagLock>() {
                @Override
                public DimensionalBagLock makeObject() {
                    return new DimensionalBagLock();
                }
            });
        }
    }
}
