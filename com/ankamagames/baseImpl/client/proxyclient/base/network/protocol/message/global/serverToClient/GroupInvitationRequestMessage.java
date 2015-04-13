package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class GroupInvitationRequestMessage extends InputOnlyProxyMessage
{
    private long m_inviterId;
    private String m_inviterName;
    private byte m_groupType;
    private boolean m_fromPartySearch;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_groupType = buffer.get();
        final byte[] name = new byte[buffer.get()];
        buffer.get(name);
        this.m_inviterName = StringUtils.fromUTF8(name);
        this.m_inviterId = buffer.getLong();
        this.m_fromPartySearch = (buffer.get() != 0);
        return true;
    }
    
    public String getInviterName() {
        return this.m_inviterName;
    }
    
    public byte getGroupType() {
        return this.m_groupType;
    }
    
    public long getInviterId() {
        return this.m_inviterId;
    }
    
    public boolean isFromPartySearch() {
        return this.m_fromPartySearch;
    }
    
    @Override
    public int getId() {
        return 502;
    }
    
    @Override
    public String toString() {
        return "GroupInvitationRequestMessage{m_inviterId=" + this.m_inviterId + ", m_inviterName='" + this.m_inviterName + '\'' + ", m_groupType=" + this.m_groupType + ", m_fromPartySearch=" + this.m_fromPartySearch + '}';
    }
}
