package com.ankamagames.baseImpl.client.proxyclient.base.chat.pipe;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class ChatPrivatePipe extends ChatBubblePipe
{
    public ChatPrivatePipe(final int id, final String internalName, final float[] color, final String name, final boolean filterable) {
        super(id, internalName, color, name, filterable);
    }
    
    @Override
    public void pushMessage(final ChatMessage message) {
        this.prePush(message);
        super.pushMessage(message);
    }
    
    @Override
    public void pushMessage(final ChatMessage message, final String subPipeKey) {
        this.prePush(message);
        super.pushMessage(message, subPipeKey);
    }
    
    private void prePush(final ChatMessage message) {
        final String name = message.getSourceName();
        if (this.getSubPipe(name) == null) {
            this.onSubPipeInexistant(name);
        }
    }
    
    @Override
    public void onSubPipeInexistant(final String subPipeKey) {
        final ChatBubblePipe subPipe = new ChatBubblePipe(-1, "subPipe".concat(subPipeKey), this.m_color, null, false);
        this.addSubPipe(subPipeKey, subPipe);
    }
}
