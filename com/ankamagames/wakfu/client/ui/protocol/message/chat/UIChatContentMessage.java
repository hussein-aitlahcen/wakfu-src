package com.ankamagames.wakfu.client.ui.protocol.message.chat;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.graphics.chat.*;

public class UIChatContentMessage extends UIMessage
{
    private String m_message;
    private AbstractChatView m_view;
    
    @Override
    public int getId() {
        return 19000;
    }
    
    public AbstractChatView getView() {
        return this.m_view;
    }
    
    public void setView(final AbstractChatView view) {
        this.m_view = view;
    }
    
    public void setMessage(final String message) {
        this.m_message = message;
    }
    
    public String getMessage() {
        return this.m_message;
    }
}
