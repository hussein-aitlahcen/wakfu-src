package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.storageBox.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.storageBox.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;

public class BrowseStorageBoxOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private StorageBox m_box;
    
    public BrowseStorageBoxOccupation(final StorageBox box) {
        super();
        this.m_box = box;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 21;
    }
    
    @Override
    public boolean isAllowed() {
        final AbstractOccupation occupation = this.m_localPlayer.getCurrentOccupation();
        return occupation == null || occupation == this;
    }
    
    @Override
    public void begin() {
        BrowseStorageBoxOccupation.m_logger.info((Object)"[STORAGE_BOX] Lancement de l'occupation");
        this.m_localPlayer.setCurrentOccupation(this);
        WakfuGameEntity.getInstance().pushFrame(NetStorageBoxMessageFrame.INSTANCE);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        BrowseStorageBoxOccupation.m_logger.info((Object)"[STORAGE_BOX] On cancel l'occupation");
        if (sendMessage) {
            this.m_box.fireAction(InteractiveElementAction.STOP_BROWSING, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
        }
        WakfuGameEntity.getInstance().removeFrame(NetStorageBoxMessageFrame.INSTANCE);
        return true;
    }
    
    @Override
    public boolean finish() {
        BrowseStorageBoxOccupation.m_logger.info((Object)"[STORAGE_BOX] On fini l'occupation");
        if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
        }
        WakfuGameEntity.getInstance().removeFrame(NetStorageBoxMessageFrame.INSTANCE);
        return true;
    }
    
    public StorageBoxCompartment onContentMessage(final RawStorageBoxCompartment raw) {
        final StorageBoxCompartment compartment = this.m_box.getInventory().get(raw.id);
        compartment.fromRaw(raw);
        return compartment;
    }
    
    public StorageBoxCompartment createCompartment(final IEStorageBoxParameter.Compartment compartment) {
        return this.m_box.getInventory().createCompartment(new IEStorageBoxParameter.Compartment(compartment.getId(), compartment.getCapacity(), compartment.getUnlockRefItemId()));
    }
    
    static {
        m_logger = Logger.getLogger((Class)BrowseStorageBoxOccupation.class);
    }
}
