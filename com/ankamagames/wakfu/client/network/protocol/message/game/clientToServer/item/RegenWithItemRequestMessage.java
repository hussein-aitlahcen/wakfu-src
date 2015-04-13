package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class RegenWithItemRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_itemRefId;
    private final long m_characterId;
    
    public RegenWithItemRequestMessage(final int itemRefId, final long characterId) {
        super();
        this.m_itemRefId = itemRefId;
        this.m_characterId = characterId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(this.m_itemRefId);
        buffer.putLong(this.m_characterId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 9106;
    }
}
