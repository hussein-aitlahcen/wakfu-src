package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;

public class CharactersListMessage extends InputOnlyProxyMessage
{
    private final ArrayList<byte[]> m_serializedCharacters;
    
    public CharactersListMessage() {
        super();
        this.m_serializedCharacters = new ArrayList<byte[]>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int numCharacters = buffer.get(), i = 0; i < numCharacters; ++i) {
            final int serializedCharacterSize = buffer.getShort();
            final byte[] serializedCharacter = new byte[serializedCharacterSize];
            buffer.get(serializedCharacter);
            this.m_serializedCharacters.add(serializedCharacter);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 2048;
    }
    
    public ArrayList<byte[]> getSerializedCharacters() {
        return this.m_serializedCharacters;
    }
}
