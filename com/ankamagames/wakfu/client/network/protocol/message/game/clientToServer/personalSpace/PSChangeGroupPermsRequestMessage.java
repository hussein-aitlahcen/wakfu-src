package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PSChangeGroupPermsRequestMessage extends OutputOnlyProxyMessage
{
    private byte m_groupId;
    private short m_permissions;
    
    public void setGroupId(final byte groupId) {
        this.m_groupId = groupId;
    }
    
    public void setPermissions(final short permissions) {
        this.m_permissions = permissions;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put(this.m_groupId);
        buffer.putShort(this.m_permissions);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10013;
    }
}
