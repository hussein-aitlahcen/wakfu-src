package com.ankamagames.wakfu.client.ui.protocol.message.chat;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.chat.*;

public class UIAddRemoveUsersFromContactsMessage extends UIMessage
{
    private short m_type;
    private WakfuUser m_addedUser;
    
    public short getType() {
        return this.m_type;
    }
    
    public void setType(final short type) {
        this.m_type = type;
    }
    
    public WakfuUser getAddedUser() {
        return this.m_addedUser;
    }
    
    public void setAddedUser(final WakfuUser addedUser) {
        this.m_addedUser = addedUser;
    }
}
