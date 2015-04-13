package com.ankamagames.wakfu.client.core.game.storageBox;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.storageBox.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.storageBox.*;

public class NetStorageBoxMessageFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final NetStorageBoxMessageFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        NetStorageBoxMessageFrame.m_logger.trace((Object)"Frame de Browsing ajout\u00e9e");
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        NetStorageBoxMessageFrame.m_logger.trace((Object)"Frame de Browsing retir\u00e9e");
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 15972: {
                final StorageBoxCompartmentContentMessage msg = (StorageBoxCompartmentContentMessage)message;
                final AbstractOccupation occupation = player.getCurrentOccupation();
                if (occupation.getOccupationTypeId() != 21) {
                    NetStorageBoxMessageFrame.m_logger.warn((Object)"[STORAGE_BOX] Reception d'un message de contenu dans un contexte ne correspondant pas");
                    return false;
                }
                final StorageBoxCompartment storageBoxCompartment = ((BrowseStorageBoxOccupation)occupation).onContentMessage(msg.getRawCompartment());
                UIStorageBoxFrame.getInstance().onCompartmentContent(storageBoxCompartment);
                NetStorageBoxMessageFrame.m_logger.info((Object)"[STORAGE_BOX]  Contenu re\u00e7u");
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetStorageBoxMessageFrame.class);
        INSTANCE = new NetStorageBoxMessageFrame();
    }
}
