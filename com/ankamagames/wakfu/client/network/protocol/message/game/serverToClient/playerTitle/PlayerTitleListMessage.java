package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.playerTitle;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PlayerTitleListMessage extends InputOnlyProxyMessage
{
    private short[] m_availableTitles;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_availableTitles = new short[buffer.getShort()];
        for (int i = 0; i < this.m_availableTitles.length; ++i) {
            this.m_availableTitles[i] = buffer.getShort();
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5506;
    }
    
    public short[] getAvailableTitles() {
        return this.m_availableTitles;
    }
}
