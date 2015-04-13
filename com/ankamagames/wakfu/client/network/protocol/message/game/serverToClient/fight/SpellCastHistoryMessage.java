package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;

public final class SpellCastHistoryMessage extends AbstractFightMessage
{
    private long m_characterId;
    private byte[] m_serializedSpellCastHistory;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_characterId = bb.getLong();
        final int dataLength = bb.getInt();
        bb.get(this.m_serializedSpellCastHistory = new byte[dataLength]);
        return false;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte[] getSerializedSpellCastHistory() {
        return this.m_serializedSpellCastHistory;
    }
    
    @Override
    public int getId() {
        return 8045;
    }
}
