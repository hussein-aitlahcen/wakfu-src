package com.ankamagames.wakfu.client.ui.protocol.message.chat;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.chat.*;

public class UIFriendUserMessage extends UIMessage
{
    private WakfuUser m_user;
    
    public WakfuUser getUser() {
        return this.m_user;
    }
    
    public void setUser(final WakfuUser user) {
        this.m_user = user;
    }
}
