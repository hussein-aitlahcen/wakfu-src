package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GroupInvitationByIdAnswerMessage extends OutputOnlyProxyMessage
{
    private final byte m_groupType;
    private final boolean m_invitationAccepted;
    private final long m_inviterId;
    
    public GroupInvitationByIdAnswerMessage(final boolean accepted, final long inviterId, final byte groupType) {
        super();
        this.m_invitationAccepted = accepted;
        this.m_groupType = groupType;
        this.m_inviterId = inviterId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(this.m_groupType);
        buffer.put((byte)(this.m_invitationAccepted ? 1 : 0));
        buffer.putLong(this.m_inviterId);
        return this.addClientHeader((byte)6, buffer.array());
    }
    
    @Override
    public final int getId() {
        return 541;
    }
    
    @Override
    public String toString() {
        return "GroupInvitationByIdAnswerMessage{m_groupType=" + this.m_groupType + ", m_invitationAccepted=" + this.m_invitationAccepted + ", m_inviterId=" + this.m_inviterId + '}';
    }
}
