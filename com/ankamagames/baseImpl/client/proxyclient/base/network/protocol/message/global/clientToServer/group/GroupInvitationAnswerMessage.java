package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class GroupInvitationAnswerMessage extends OutputOnlyProxyMessage
{
    public boolean m_sendToGlobalServer;
    private byte m_groupType;
    private boolean m_invitationAccepted;
    private String m_inviterName;
    private String m_groupName;
    private long m_inviterId;
    
    public GroupInvitationAnswerMessage() {
        super();
        this.m_sendToGlobalServer = true;
        this.m_inviterId = -1L;
    }
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_inviterName);
        final byte[] groupName = StringUtils.toUTF8(this.m_groupName);
        final int sizeDatas = 3 + name.length + 1 + groupName.length + 8;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.put(this.m_groupType);
        buffer.put((byte)(this.m_invitationAccepted ? 1 : 0));
        buffer.put((byte)name.length);
        buffer.put(name);
        buffer.put((byte)groupName.length);
        buffer.put(groupName);
        buffer.putLong(this.m_inviterId);
        if (this.m_sendToGlobalServer) {
            return this.addClientHeader((byte)6, buffer.array());
        }
        return this.addClientHeader((byte)2, buffer.array());
    }
    
    @Override
    public final int getId() {
        return 503;
    }
    
    public void setGroupType(final byte groupType) {
        this.m_groupType = groupType;
    }
    
    public void setInvitationAccepted(final boolean invitationAccepted) {
        this.m_invitationAccepted = invitationAccepted;
    }
    
    public void setInviterName(final String inviterName) {
        this.m_inviterName = inviterName;
    }
    
    public void setInviterId(final long inviterId) {
        this.m_inviterId = inviterId;
    }
    
    public void setGroupName(final String groupName) {
        this.m_groupName = groupName;
    }
    
    public void setSendToGlobalServer(final boolean sendToGlobalServer) {
        this.m_sendToGlobalServer = sendToGlobalServer;
    }
}
