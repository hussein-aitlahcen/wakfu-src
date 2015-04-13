package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagPermissionsMessage extends OutputOnlyProxyMessage
{
    private byte[] m_serializedPermissions;
    
    public void setSerializedPermissions(final byte[] serializedPermissions) {
        this.m_serializedPermissions = serializedPermissions;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4 + this.m_serializedPermissions.length);
        buffer.putInt(this.m_serializedPermissions.length);
        buffer.put(this.m_serializedPermissions);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10043;
    }
}
