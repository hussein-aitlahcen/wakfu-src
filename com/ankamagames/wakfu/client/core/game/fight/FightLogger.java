package com.ankamagames.wakfu.client.core.game.fight;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class FightLogger
{
    private static final Logger m_logger;
    
    public void info(final Object message) {
        try {
            final ChatMessage chatMessage = new ChatMessage(message.toString());
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
        catch (Exception e) {
            FightLogger.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public void error(final Object message) {
        try {
            final ChatMessage chatMessage = new ChatMessage(message.toString());
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
        catch (Exception e) {
            FightLogger.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightLogger.class);
    }
}
