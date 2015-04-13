package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GroupExcludeMemberMessage extends OutputOnlyProxyMessage
{
    public boolean m_sendToGlobalServer;
    private long m_groupId;
    private long m_characterId;
    
    public GroupExcludeMemberMessage() {
        super();
        this.m_sendToGlobalServer = true;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(this.m_groupId);
        bb.putLong(this.m_characterId);
        if (this.m_sendToGlobalServer) {
            return this.addClientHeader((byte)6, bb.array());
        }
        return this.addClientHeader((byte)2, bb.array());
    }
    
    @Override
    public final int getId() {
        return 505;
    }
    
    public void setGroupId(final long groupId) {
        this.m_groupId = groupId;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setSendToGlobalServer(final boolean sendToGlobalServer) {
        this.m_sendToGlobalServer = sendToGlobalServer;
    }
}
