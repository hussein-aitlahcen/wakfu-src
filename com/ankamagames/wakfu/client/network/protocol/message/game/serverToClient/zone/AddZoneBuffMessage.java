package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.zone;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AddZoneBuffMessage extends InputOnlyProxyMessage
{
    private int m_buffId;
    private int m_buffRemainingTime;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length != 8) {
            return false;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_buffId = buffer.getInt();
        this.m_buffRemainingTime = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 15200;
    }
    
    public int getBuffId() {
        return this.m_buffId;
    }
    
    public int getBuffRemainingTime() {
        return this.m_buffRemainingTime;
    }
}
