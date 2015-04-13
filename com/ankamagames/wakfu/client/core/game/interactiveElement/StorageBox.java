package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.storageBox.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class StorageBox extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEStorageBoxParameter m_info;
    private StorageBoxInventory m_inventory;
    private final BinarSerialPart SYNCHRO_PART;
    
    public StorageBox() {
        super();
        this.SYNCHRO_PART = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Pas de serialization dans le client");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                for (int numTabUnlocked = buffer.getInt(), i = 0; i < numTabUnlocked; ++i) {
                    StorageBox.this.m_inventory.createCompartment(StorageBox.this.m_info.getFromId(buffer.getInt()));
                }
            }
            
            @Override
            public int expectedSize() {
                return 4 + StorageBox.this.m_inventory.size() * 4;
            }
        };
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case START_BROWSING: {
                this.runScript(action);
                final BasicOccupation occupation = new BrowseStorageBoxOccupation(this);
                if (!occupation.isAllowed()) {
                    StorageBox.m_logger.error((Object)"[STORAGE_BOX] Impossible de d\u00e9marrer le browsing");
                    return false;
                }
                occupation.begin();
                this.sendActionMessage(action);
                UIStorageBoxFrame.getInstance().setStorageBox(this);
                WakfuGameEntity.getInstance().pushFrame(UIStorageBoxFrame.getInstance());
                return true;
            }
            case STOP_BROWSING: {
                this.runScript(action);
                if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance())) {
                    WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
                }
                this.sendActionMessage(action);
                WakfuGameEntity.getInstance().getLocalPlayer().finishCurrentOccupation();
                return true;
            }
            default: {
                StorageBox.m_logger.error((Object)("Impossible d'effectuer l'action " + action), (Throwable)new IllegalArgumentException());
                return false;
            }
        }
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SYNCHRO_PART;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.START_BROWSING;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_BROWSING, InteractiveElementAction.STOP_BROWSING };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        this.setUseSpecificAnimTransition(true);
        assert this.m_info == null;
        assert this.m_inventory == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(false);
        this.setOverHeadable(false);
        this.setUseSpecificAnimTransition(false);
        this.m_info = null;
        this.m_inventory = null;
    }
    
    @Override
    public void initializeWithParameter() {
        this.m_info = (IEStorageBoxParameter)IEParametersManager.INSTANCE.getParam(IETypes.STORAGE_BOX, Integer.parseInt(this.m_parameter));
        if (this.m_info == null) {
            StorageBox.m_logger.error((Object)("Impossible de trouver le param\u00e8tre " + this.m_parameter));
            return;
        }
        this.m_inventory = new StorageBoxInventory(this.m_info.size());
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_info.getVisualId());
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(visual.getMruGfx());
        action.setTextKey("desc.mru." + visual.getMruLabelKey());
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(104, this.m_info.getId(), new Object[0]);
    }
    
    public StorageBoxInventory getInventory() {
        return this.m_inventory;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    public IEStorageBoxParameter getInfo() {
        return this.m_info;
    }
    
    @Override
    public String toString() {
        return "StorageBox{m_info=" + this.m_info + ", m_inventory=" + this.m_inventory + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)StorageBox.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            StorageBox ie;
            try {
                ie = (StorageBox)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                StorageBox.m_logger.error((Object)"Erreur lors de l'extraction du pool", (Throwable)e);
                ie = new StorageBox();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<StorageBox>() {
                @Override
                public StorageBox makeObject() {
                    return new StorageBox();
                }
            });
        }
    }
}
