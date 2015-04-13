package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ClientCharacterUpdateMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private byte[] m_serializedParts;
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setSerializedParts(final byte[] serializedParts) {
        this.m_serializedParts = serializedParts;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte[] getSerializedParts() {
        return this.m_serializedParts;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        buffer.get(this.m_serializedParts = new byte[buffer.getShort()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 20002;
    }
}
