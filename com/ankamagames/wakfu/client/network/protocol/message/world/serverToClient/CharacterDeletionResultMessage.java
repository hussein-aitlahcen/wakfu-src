package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class CharacterDeletionResultMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private boolean m_characterDeletionSuccessful;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_characterDeletionSuccessful = (buffer.get() > 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 2052;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public boolean isCharacterDeletionSuccessful() {
        return this.m_characterDeletionSuccessful;
    }
}
