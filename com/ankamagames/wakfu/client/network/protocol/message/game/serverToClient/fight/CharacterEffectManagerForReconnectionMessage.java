package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import java.nio.*;
import java.util.*;

public final class CharacterEffectManagerForReconnectionMessage extends AbstractFightMessage
{
    private long m_characterId;
    private byte[] m_serializedEffects;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeFightHeader(bb);
        this.m_characterId = bb.getLong();
        bb.get(this.m_serializedEffects = new byte[bb.getShort()]);
        return false;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte[] getSerializedEffects() {
        return this.m_serializedEffects;
    }
    
    @Override
    public int getId() {
        return 8043;
    }
    
    @Override
    public String toString() {
        return "CharacterEffectManagerForReconnectionMessage{m_characterId=" + this.m_characterId + ", m_serializedEffects=" + Arrays.toString(this.m_serializedEffects) + '}';
    }
}
