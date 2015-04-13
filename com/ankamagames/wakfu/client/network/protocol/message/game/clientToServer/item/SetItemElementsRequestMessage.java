package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SetItemElementsRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemUid;
    private byte m_damageElementMask;
    private byte m_resElementMask;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putLong(this.m_itemUid);
        buffer.put(this.m_damageElementMask);
        buffer.put(this.m_resElementMask);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setItemUid(final long itemUid) {
        this.m_itemUid = itemUid;
    }
    
    public void setDamageElementMask(final byte damageElementMask) {
        this.m_damageElementMask = damageElementMask;
    }
    
    public void setResElementMask(final byte resElementMask) {
        this.m_resElementMask = resElementMask;
    }
    
    @Override
    public int getId() {
        return 13013;
    }
}
