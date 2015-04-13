package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class MonsterStateMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private byte m_stateId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_stateId = buffer.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 4112;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte getStateId() {
        return this.m_stateId;
    }
}
