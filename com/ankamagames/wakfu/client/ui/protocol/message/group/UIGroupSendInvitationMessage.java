package com.ankamagames.wakfu.client.ui.protocol.message.group;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;

public class UIGroupSendInvitationMessage extends UIMessage
{
    private GroupType m_groupType;
    private String m_name;
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public GroupType getGroupType() {
        return this.m_groupType;
    }
    
    public void setGroupType(final GroupType groupType) {
        this.m_groupType = groupType;
    }
    
    @Override
    public int getId() {
        return 17601;
    }
}
