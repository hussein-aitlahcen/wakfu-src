package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.weather;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class WeatherHistoryAnswerMessage extends InputOnlyProxyMessage
{
    private byte[] m_history;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final int size = buffer.getShort() & 0xFFFF;
        if (size > 0) {
            buffer.get(this.m_history = new byte[size]);
        }
        return true;
    }
    
    public byte[] getHistory() {
        return this.m_history;
    }
    
    @Override
    public int getId() {
        return 9602;
    }
}
