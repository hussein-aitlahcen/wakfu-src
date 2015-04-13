package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.costume;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PutOnCostumeMessage extends OutputOnlyProxyMessage
{
    private final long m_characterId;
    private final int m_refItemId;
    
    public PutOnCostumeMessage(final long id, final int refItemId) {
        super();
        this.m_characterId = id;
        this.m_refItemId = refItemId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putLong(this.m_characterId);
        buffer.putInt(this.m_refItemId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15811;
    }
}
