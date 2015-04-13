package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagPermissionsUpdateMessage extends InputOnlyProxyMessage
{
    private long m_bagOwnerId;
    private byte[] m_serializedPermissions;
    
    public long getBagOwnerId() {
        return this.m_bagOwnerId;
    }
    
    public byte[] getSerializedPermissions() {
        return this.m_serializedPermissions;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_bagOwnerId = buffer.getLong();
        buffer.get(this.m_serializedPermissions = new byte[buffer.getInt()]);
        return false;
    }
    
    @Override
    public int getId() {
        return 10044;
    }
}
