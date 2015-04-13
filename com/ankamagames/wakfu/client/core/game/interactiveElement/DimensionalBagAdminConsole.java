package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagAdminConsole extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)0);
        this.setVisible(true);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.dimensionalBagAdminConsole");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        DimensionalBagAdminConsole.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return BinarSerialPart.EMPTY;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        WakfuClientInstance.getInstance();
        final WakfuGameEntity entity = WakfuClientInstance.getGameEntity();
        switch (action) {
            case ACTIVATE: {
                if (!entity.hasFrame(UIDimensionalBagRoomManagerFrame.getInstance())) {
                    entity.pushFrame(UIDimensionalBagRoomManagerFrame.getInstance());
                }
                else {
                    entity.removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
                }
                return true;
            }
            case ACTIVATE2: {
                if (!entity.hasFrame(UIDimensionalBagAppearanceManagerFrame.getInstance())) {
                    entity.pushFrame(UIDimensionalBagAppearanceManagerFrame.getInstance());
                }
                else {
                    entity.removeFrame(UIDimensionalBagAppearanceManagerFrame.getInstance());
                }
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE, InteractiveElementAction.ACTIVATE2 };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean ownBag = player.getVisitingDimentionalBag() == player.getOwnedDimensionalBag();
        final MRUGenericInteractiveAction manageGems = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        manageGems.setActionToExecute(InteractiveElementAction.ACTIVATE);
        manageGems.setGfxId(MRUGfxConstants.GEMME.m_id);
        manageGems.setName("desc.mru.manageRooms");
        manageGems.setEnabled(ownBag);
        final MRUGenericInteractiveAction manageBag = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
        manageBag.setActionToExecute(InteractiveElementAction.ACTIVATE2);
        manageBag.setGfxId(MRUGfxConstants.BAG.m_id);
        manageBag.setName("desc.mru.manageBagAppearance");
        manageBag.setEnabled(ownBag);
        return new AbstractMRUAction[] { manageGems, manageBag };
    }
    
    @Override
    public byte getHeight() {
        return 4;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagAdminConsole.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DimensionalBagAdminConsole console;
            try {
                console = (DimensionalBagAdminConsole)Factory.m_pool.borrowObject();
                console.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagAdminConsole.m_logger.error((Object)"Erreur lors de l'extraction d'un DimensionalBagAdminConsole du pool", (Throwable)e);
                console = new DimensionalBagAdminConsole();
            }
            return console;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagAdminConsole>() {
                @Override
                public DimensionalBagAdminConsole makeObject() {
                    return new DimensionalBagAdminConsole();
                }
            });
        }
    }
}
