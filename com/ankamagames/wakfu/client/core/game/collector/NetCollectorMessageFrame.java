package com.ankamagames.wakfu.client.core.game.collector;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.collector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;

public class NetCollectorMessageFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final NetCollectorMessageFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        NetCollectorMessageFrame.m_logger.trace((Object)"Frame de Browsing ajout\u00e9e");
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        NetCollectorMessageFrame.m_logger.trace((Object)"Frame de Browsing retir\u00e9e");
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 15730: {
                final CollectorContentMessage msg = (CollectorContentMessage)message;
                final AbstractOccupation occupation = player.getCurrentOccupation();
                if (occupation.getOccupationTypeId() != 17) {
                    NetCollectorMessageFrame.m_logger.warn((Object)"Reception d'un message de contenu de r\u00e9ceptacle ne dans un contexte ne correspondant pas");
                    return false;
                }
                ((BrowseCollectorOccupation)occupation).onContentMessage(msg.getContent());
                NetCollectorMessageFrame.m_logger.info((Object)"Contenu du r\u00e9ceptacle re\u00e7u");
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
        m_logger = Logger.getLogger((Class)NetCollectorMessageFrame.class);
        INSTANCE = new NetCollectorMessageFrame();
    }
}
