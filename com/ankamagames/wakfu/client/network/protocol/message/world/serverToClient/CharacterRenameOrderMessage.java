package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class CharacterRenameOrderMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 2070;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
}
