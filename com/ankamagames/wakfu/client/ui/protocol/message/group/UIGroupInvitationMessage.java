package com.ankamagames.wakfu.client.ui.protocol.message.group;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;

public class UIGroupInvitationMessage extends UIMessage
{
    private GroupType m_groupType;
    private String m_inviterName;
    private long m_inviterId;
    
    public GroupType getGroupType() {
        return this.m_groupType;
    }
    
    public void setGroupType(final GroupType groupType) {
        this.m_groupType = groupType;
    }
    
    @Override
    public int getId() {
        return 17600;
    }
    
    public String getInviterName() {
        return this.m_inviterName;
    }
    
    public void setInviterId(final long inviterId) {
        this.m_inviterId = inviterId;
    }
    
    public long getInviterId() {
        return this.m_inviterId;
    }
    
    public void setInviterName(final String inviterName) {
        this.m_inviterName = inviterName;
    }
}
