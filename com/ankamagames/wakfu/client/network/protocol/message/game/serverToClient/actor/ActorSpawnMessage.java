package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;
import java.util.*;

public class ActorSpawnMessage extends InputOnlyProxyMessage
{
    private boolean m_myFightSpawn;
    private TByteArrayList m_characterTypes;
    private TLongArrayList m_characterIds;
    private List<byte[]> m_characterSerialized;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_myFightSpawn = (buffer.get() == 1);
        final int charactersCount = buffer.get() & 0xFF;
        this.m_characterTypes = new TByteArrayList(charactersCount);
        this.m_characterIds = new TLongArrayList(charactersCount);
        this.m_characterSerialized = new ArrayList<byte[]>(charactersCount);
        for (int i = 0; i < charactersCount; ++i) {
            this.m_characterTypes.add(buffer.get());
            this.m_characterIds.add(buffer.getLong());
            final int serializedSize = buffer.getShort() & 0xFFFF;
            final byte[] serializedCharacter = new byte[serializedSize];
            buffer.get(serializedCharacter);
            this.m_characterSerialized.add(serializedCharacter);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 4102;
    }
    
    public boolean isMyFightSpawn() {
        return this.m_myFightSpawn;
    }
    
    public TByteArrayList getCharacterTypes() {
        return this.m_characterTypes;
    }
    
    public TLongArrayList getCharacterIds() {
        return this.m_characterIds;
    }
    
    public List<byte[]> getCharacterSerialized() {
        return this.m_characterSerialized;
    }
}
