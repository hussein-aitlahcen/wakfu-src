package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public final class LevelUpNewAptitudeResultMessage extends InputOnlyProxyMessage
{
    private TIntShortHashMap m_results;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final short size = buffer.getShort();
        this.m_results = new TIntShortHashMap(size);
        for (int i = 0; i < size; ++i) {
            this.m_results.put(buffer.getInt(), buffer.getShort());
        }
        return false;
    }
    
    public TIntShortHashMap getResults() {
        return this.m_results;
    }
    
    @Override
    public int getId() {
        return 8417;
    }
    
    @Override
    public String toString() {
        return "LevelUpNewAptitudeResultMessage{m_results=" + this.m_results + '}';
    }
}
