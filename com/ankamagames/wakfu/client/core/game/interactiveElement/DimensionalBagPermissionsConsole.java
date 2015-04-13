package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DimensionalBagPermissionsConsole extends WakfuClientMapInteractiveElement
{
    protected static final Logger m_logger;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(true);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.dimensionalBagPermissionsConsole");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return BinarSerialPart.EMPTY;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        switch (action) {
            case ACTIVATE: {
                WakfuClientInstance.getInstance();
                final WakfuGameEntity entity = WakfuClientInstance.getGameEntity();
                final UIDimensionalBagRoomAdministrationFrame frame = UIDimensionalBagRoomAdministrationFrame.getInstance();
                if (!entity.hasFrame(frame)) {
                    entity.pushFrame(frame);
                }
                else {
                    entity.removeFrame(frame);
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
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean ownBag = player.getVisitingDimentionalBag() == player.getOwnedDimensionalBag();
        final AbstractMRUAction action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setEnabled(ownBag);
        return new AbstractMRUAction[] { action };
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagPermissionsConsole.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DimensionalBagPermissionsConsole console;
            try {
                console = (DimensionalBagPermissionsConsole)Factory.m_pool.borrowObject();
                console.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                DimensionalBagPermissionsConsole.m_logger.error((Object)("Erreur lors de l'extraction d'un " + DimensionalBagPermissionsConsole.class + " du pool"), (Throwable)e);
                console = new DimensionalBagPermissionsConsole();
            }
            return console;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<DimensionalBagPermissionsConsole>() {
                @Override
                public DimensionalBagPermissionsConsole makeObject() {
                    return new DimensionalBagPermissionsConsole();
                }
            });
        }
    }
}
