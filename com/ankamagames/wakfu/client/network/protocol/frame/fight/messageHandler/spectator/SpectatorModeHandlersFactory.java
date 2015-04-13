package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.spectator;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;

public final class SpectatorModeHandlersFactory
{
    private static Logger m_logger;
    
    public static <M extends Message, Fi extends FightInfo> void createHandlerForThisFrame(final int msgType, final FightManagementFrame<Fi> frame) {
        FightMessageHandler handler = null;
        switch (msgType) {
            case 8100: {
                handler = new TableTurnBeginMessageHandler();
                break;
            }
            case 8300: {
                handler = new EndFightMessageHandler();
                break;
            }
            case 8104: {
                handler = new FighterTurnBeginMessageHandler();
                break;
            }
            default: {
                SpectatorModeHandlersFactory.m_logger.warn((Object)("ATTENTION : l'id de message pass\u00e9 en parametre n'est pas g\u00e9r\u00e9 par la factory : " + msgType));
                handler = CommonHandlersFactory.returnTrueHandler();
                break;
            }
        }
        handler.setHandledMessageId(msgType);
        frame.addHandler(handler);
    }
    
    static {
        SpectatorModeHandlersFactory.m_logger = Logger.getLogger((Class)SpectatorModeHandlersFactory.class);
    }
}
