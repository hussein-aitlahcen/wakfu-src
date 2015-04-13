package com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class CharacterRenameRequestMessage extends OutputOnlyProxyMessage
{
    private long m_characterId;
    private String m_characterName;
    
    @Override
    public byte[] encode() {
        final byte[] serializedCharacterName = StringUtils.toUTF8(this.m_characterName);
        final ByteBuffer buffer = ByteBuffer.allocate(10 + serializedCharacterName.length);
        buffer.putLong(this.m_characterId);
        buffer.putShort((short)(serializedCharacterName.length & 0xFF));
        buffer.put(serializedCharacterName);
        return this.addClientHeader((byte)2, buffer.array());
    }
    
    @Override
    public int getId() {
        return 2071;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setCharacterName(final String characterName) {
        this.m_characterName = characterName;
    }
}
