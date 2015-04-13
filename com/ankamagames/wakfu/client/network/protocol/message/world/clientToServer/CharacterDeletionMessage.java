package com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CharacterDeletionMessage extends OutputOnlyProxyMessage
{
    private long m_characterId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_characterId);
        return this.addClientHeader((byte)2, buffer.array());
    }
    
    @Override
    public int getId() {
        return 2051;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
}
