package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class HavenWorldBuildingBoard extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEHavenWorldBuildingBoardParameter m_param;
    private long m_buildingUid;
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Ne devrait pas passer par ici");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                HavenWorldBuildingBoard.this.m_buildingUid = buffer.getLong();
            }
        };
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        this.sendActionMessage(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.READ;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.READ, InteractiveElementAction.UNLOCK };
    }
    
    @Override
    public void initializeWithParameter() {
        this.m_param = (IEHavenWorldBuildingBoardParameter)IEParametersManager.INSTANCE.getParam(IETypes.HAVEN_WORLD_BUILDING_BOARD, Integer.parseInt(this.m_parameter));
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_param.getVisualId());
        final ArrayList<AbstractMRUAction> actions = new ArrayList<AbstractMRUAction>();
        final MRUHavenWorldBuildingBoardAction read = new MRUHavenWorldBuildingBoardAction("informations", MRUGfxConstants.BOOK.m_id);
        read.setActionToExecute(InteractiveElementAction.READ);
        actions.add(read);
        if (WakfuGameEntity.getInstance().getLocalPlayer().getAccountInformationHandler().hasAdminRight(AdminRightsEnum.EVOLVE_HAVEN_WORLD_BUILDING)) {
            final MRUGenericInteractiveAction lock = new MRUGenericInteractiveAction();
            lock.setActionToExecute(InteractiveElementAction.UNLOCK);
            lock.setName("XXX---> 3V01U7!0N <---XXX");
            lock.setGfxId(MRUGfxConstants.MUSHROOM.m_id);
            actions.add(lock);
        }
        return actions.toArray(new AbstractMRUAction[actions.size()]);
    }
    
    @Override
    public String getName() {
        return "TODO";
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public IEHavenWorldBuildingBoardParameter getParam() {
        return this.m_param;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_param = null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_param = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldBuildingBoard.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            HavenWorldBuildingBoard ie;
            try {
                ie = (HavenWorldBuildingBoard)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                HavenWorldBuildingBoard.m_logger.error((Object)"Erreur lors de l'extraction d'une CharacterStatue du pool", (Throwable)e);
                ie = new HavenWorldBuildingBoard();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<HavenWorldBuildingBoard>() {
                @Override
                public HavenWorldBuildingBoard makeObject() {
                    return new HavenWorldBuildingBoard();
                }
            });
        }
    }
}
