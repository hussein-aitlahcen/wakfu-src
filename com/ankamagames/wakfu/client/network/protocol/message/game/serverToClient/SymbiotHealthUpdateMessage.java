package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SymbiotHealthUpdateMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int[] m_health;
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int[] getValue() {
        return this.m_health;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_health = new int[buffer.get()];
        for (int i = 0; i < this.m_health.length; ++i) {
            this.m_health[i] = buffer.getInt();
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5402;
    }
}
