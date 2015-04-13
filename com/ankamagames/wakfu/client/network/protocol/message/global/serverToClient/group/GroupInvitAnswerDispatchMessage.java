package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GroupInvitAnswerDispatchMessage extends InputOnlyProxyMessage
{
    private boolean m_accepted;
    private long m_invitedId;
    
    public GroupInvitAnswerDispatchMessage() {
        super();
        this.m_accepted = false;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_accepted = (buffer.get() != 0);
        this.m_invitedId = buffer.getLong();
        return false;
    }
    
    public boolean isInvitationAccepted() {
        return this.m_accepted;
    }
    
    public long getInvitedId() {
        return this.m_invitedId;
    }
    
    @Override
    public int getId() {
        return 12404;
    }
}
