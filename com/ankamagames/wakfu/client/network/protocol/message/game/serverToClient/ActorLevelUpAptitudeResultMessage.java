package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class ActorLevelUpAptitudeResultMessage extends InputOnlyProxyMessage
{
    private TShortShortHashMap m_results;
    private boolean m_consumePoints;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final short size = buffer.getShort();
        this.m_results = new TShortShortHashMap(size);
        for (int i = 0; i < size; ++i) {
            this.m_results.put(buffer.getShort(), buffer.getShort());
        }
        this.m_consumePoints = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 8408;
    }
    
    public TShortShortHashMap getResults() {
        return this.m_results;
    }
    
    public boolean isConsumePoints() {
        return this.m_consumePoints;
    }
}
