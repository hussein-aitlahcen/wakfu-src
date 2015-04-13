package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MergeIntoItemSetRequestMessage extends OutputOnlyProxyMessage
{
    private short m_itemSetId;
    private long m_characterId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putShort(this.m_itemSetId);
        buffer.putLong(this.m_characterId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5249;
    }
    
    public void setItemSetId(final short itemSetId) {
        this.m_itemSetId = itemSetId;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MergeIntoItemSetRequestMessage");
        sb.append("{m_itemSetId=").append(this.m_itemSetId);
        sb.append('}');
        return sb.toString();
    }
}
