package com.ankamagames.wakfu.client.ui.protocol.message.chat;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.chat.*;

public class UIChatRemoveViewMessage extends UIMessage
{
    private ChatView m_view;
    
    @Override
    public int getId() {
        return 19015;
    }
    
    public ChatView getView() {
        return this.m_view;
    }
    
    public void setView(final ChatView view) {
        this.m_view = view;
    }
}
