package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CharacterUpdateMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private byte[] m_serializedData;
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte[] getSerializedData() {
        return this.m_serializedData;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        buffer.get(this.m_serializedData = new byte[buffer.getShort()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 4130;
    }
}
