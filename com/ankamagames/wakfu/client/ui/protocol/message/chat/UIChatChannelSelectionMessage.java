package com.ankamagames.wakfu.client.ui.protocol.message.chat;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.graphics.chat.*;

public class UIChatChannelSelectionMessage extends UIMessage
{
    private ChatPipeWrapper m_pipeWrapper;
    private AbstractChatView m_view;
    
    @Override
    public int getId() {
        return 19011;
    }
    
    public ChatPipeWrapper getPipeWrapper() {
        return this.m_pipeWrapper;
    }
    
    public void setPipeWrapper(final ChatPipeWrapper pipeWrapper) {
        this.m_pipeWrapper = pipeWrapper;
    }
    
    public AbstractChatView getView() {
        return this.m_view;
    }
    
    public void setView(final AbstractChatView view) {
        this.m_view = view;
    }
}
