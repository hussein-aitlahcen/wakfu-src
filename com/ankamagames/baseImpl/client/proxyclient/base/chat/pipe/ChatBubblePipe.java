package com.ankamagames.baseImpl.client.proxyclient.base.chat.pipe;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.bubble.*;

public class ChatBubblePipe extends ChatSimplePipe
{
    public ChatBubblePipe(final int id, final String internalName, final float[] color, final String name, final boolean filterable) {
        super(id, internalName, color, name, filterable);
    }
    
    @Override
    public void pushMessage(final ChatMessage message) {
        final ChatMessage msg = this.transformMessage(message);
        if (msg != null) {
            super.pushMessage(msg);
        }
    }
    
    @Override
    public void pushMessage(final ChatMessage message, final String subPipeKey) {
        final ChatMessage msg = this.transformMessage(message);
        super.pushMessage(msg, subPipeKey);
    }
    
    private ChatMessage transformMessage(ChatMessage message) {
        final AbstractChatBubbleManager bubbleManager = ChatManager.getInstance().getChatBubbleManager();
        if (!this.m_listeners.isEmpty() && bubbleManager != null) {
            message = bubbleManager.pushMessage(message, this.m_color);
        }
        return message;
    }
}
