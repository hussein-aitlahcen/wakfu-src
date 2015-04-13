package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.storageBox.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;

public class BrowseGuildStorageBoxOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private final GuildStorageBox m_box;
    
    public BrowseGuildStorageBoxOccupation(final GuildStorageBox box) {
        super();
        this.m_box = box;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 23;
    }
    
    @Override
    public boolean isAllowed() {
        final AbstractOccupation occupation = this.m_localPlayer.getCurrentOccupation();
        return occupation == null || occupation == this;
    }
    
    @Override
    public void begin() {
        BrowseGuildStorageBoxOccupation.m_logger.info((Object)"[STORAGE_BOX] Lancement de l'occupation");
        this.m_localPlayer.setCurrentOccupation(this);
        WakfuGameEntity.getInstance().pushFrame(NetGuildStorageFrame.INSTANCE);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        BrowseGuildStorageBoxOccupation.m_logger.info((Object)"[STORAGE_BOX] On cancel l'occupation");
        if (sendMessage) {
            this.m_box.fireAction(InteractiveElementAction.STOP_BROWSING, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
        }
        WakfuGameEntity.getInstance().removeFrame(NetGuildStorageFrame.INSTANCE);
        WakfuGameEntity.getInstance().removeFrame(NetStorageBoxMessageFrame.INSTANCE);
        return true;
    }
    
    @Override
    public boolean finish() {
        BrowseGuildStorageBoxOccupation.m_logger.info((Object)"[STORAGE_BOX] On fini l'occupation");
        if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
        }
        WakfuGameEntity.getInstance().removeFrame(NetGuildStorageFrame.INSTANCE);
        WakfuGameEntity.getInstance().removeFrame(NetStorageBoxMessageFrame.INSTANCE);
        return true;
    }
    
    public static GuildStorageCompartment onContentMessage(final RawGuildStorageCompartment serializedContent) {
        final GuildStorageCompartmentType type = GuildStorageCompartmentType.getFromId(serializedContent.id);
        if (type == null) {
            return null;
        }
        final GuildStorageCompartment compartment = new GuildStorageCompartment(type);
        compartment.fromRaw(serializedContent);
        return compartment;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BrowseGuildStorageBoxOccupation.class);
    }
}
