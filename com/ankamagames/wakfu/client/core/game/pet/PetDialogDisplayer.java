package com.ankamagames.wakfu.client.core.game.pet;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class PetDialogDisplayer
{
    protected static final Logger m_logger;
    private final LinkedList<UITutorialFrame.PetMessage> m_messageQueue;
    private UITutorialFrame.PetMessage m_currentMessage;
    
    public PetDialogDisplayer() {
        super();
        this.m_messageQueue = new LinkedList<UITutorialFrame.PetMessage>();
    }
    
    public void pushMessage(final UITutorialFrame.PetMessage message) {
        final String text = message.getText();
        final boolean forced = message.isForced();
        final long duration = message.getDuration();
        if (!forced && !WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.TIPS_ACTIVATED)) {
            return;
        }
        if (this.m_currentMessage == null || (!this.m_currentMessage.isForced() && forced)) {
            this.m_currentMessage = message;
            PropertiesProvider.getInstance().setPropertyValue("petBubbleMessage", text);
            if (duration != -1L) {
                final long realDuration = (duration > 0L) ? duration : (TextUtils.getTextDuration(text) / 2);
                ProcessScheduler.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (PetDialogDisplayer.this.m_currentMessage == message) {
                            PetDialogDisplayer.this.setNextMessage();
                        }
                    }
                }, realDuration, 1);
            }
            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("gelutin"), text);
            chatMessage.setColor(ChatConstants.CHAT_DEFAULT_COLOR);
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
        else {
            this.m_messageQueue.addLast(message);
        }
    }
    
    public void setNextMessage() {
        this.m_currentMessage = null;
        if (!this.m_messageQueue.isEmpty()) {
            this.pushMessage(this.m_messageQueue.poll());
        }
        else {
            UITutorialFrame.getInstance().onDialogEnd();
        }
    }
    
    public void clean() {
        this.m_currentMessage = null;
        this.m_messageQueue.clear();
        UITutorialFrame.getInstance().onDialogEnd();
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetDialogDisplayer.class);
    }
}
