package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PSKickUserRequestMessage extends OutputOnlyProxyMessage
{
    private long m_characterId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_characterId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    @Override
    public int getId() {
        return 10015;
    }
}
