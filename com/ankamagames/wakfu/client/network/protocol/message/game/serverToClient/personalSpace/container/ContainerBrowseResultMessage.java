package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.container;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ContainerBrowseResultMessage extends InputOnlyProxyMessage
{
    private byte m_resultCode;
    private long m_containerGuid;
    
    public byte getResultCode() {
        return this.m_resultCode;
    }
    
    public long getContainerGuid() {
        return this.m_containerGuid;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_resultCode = buffer.get();
        this.m_containerGuid = buffer.getLong();
        if (this.m_resultCode == 0) {
            final byte[] serializedContainerInventory = new byte[buffer.getShort() & 0xFFFF];
            buffer.get(serializedContainerInventory);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 10056;
    }
}
