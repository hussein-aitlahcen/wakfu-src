package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public abstract class AbstractCharacterDataMessage extends AbstractFightMessage
{
    private long m_characterId;
    private byte[] m_data;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_characterId = bb.getLong();
        bb.get(this.m_data = new byte[bb.getShort()]);
        return false;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
}
