package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class ActorUpdatePropertiesMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<byte[]> m_serializedCharactersInfos;
    
    public ActorUpdatePropertiesMessage() {
        super();
        this.m_serializedCharactersInfos = new TLongObjectHashMap<byte[]>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte charactersCount = buffer.get();
        for (int i = 0; i < charactersCount; ++i) {
            final long characterId = buffer.getLong();
            final byte[] serializedPropertiesCharacterInfo = new byte[buffer.getShort() & 0xFFFF];
            buffer.get(serializedPropertiesCharacterInfo);
            this.m_serializedCharactersInfos.put(characterId, serializedPropertiesCharacterInfo);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4216;
    }
    
    public TLongObjectHashMap<byte[]> getSerializedPropertiesActorInfos() {
        return this.m_serializedCharactersInfos;
    }
}
