package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.pet;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class PetChangeNameRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemId;
    private String m_creatureName;
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_creatureName);
        final ByteBuffer buffer = ByteBuffer.allocate(8 + name.length);
        buffer.putLong(this.m_itemId);
        buffer.put(name);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15981;
    }
    
    public void setItemId(final long itemId) {
        this.m_itemId = itemId;
    }
    
    public void setCreatureName(final String creatureName) {
        this.m_creatureName = creatureName;
    }
}
